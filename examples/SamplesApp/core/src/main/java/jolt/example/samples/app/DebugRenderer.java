package jolt.example.samples.app;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.ArrayList;
import jolt.BodyManagerDrawSettings;
import jolt.DebugArrayTriangle;
import jolt.DebugRendererEm;
import jolt.DebugRendererTriangle;
import jolt.DebugRendererVertex;
import jolt.ECastShadow;
import jolt.EDrawMode;
import jolt.jolt.Jolt;
import jolt.jolt.core.Color;
import jolt.jolt.math.Float2;
import jolt.jolt.math.Float3;
import jolt.jolt.math.Mat44;
import jolt.jolt.math.Vec3;
import jolt.jolt.math.Vec4;
import jolt.jolt.physics.PhysicsSystem;

/**
 * DebugRenderer is a class that renders debug information in a game using the Jolt physics engine.
 * It is slow because it updates the mesh every frame. Caching may speed it up
 */
public class DebugRenderer extends DebugRendererEm {

    private ModelBatch batch;
    protected Environment environment;

    private ArrayList<ModelProvider> modelPool = new ArrayList<>();
    private ArrayList<ModelProvider> modelRenderer = new ArrayList<>();

    private Vec3 v0, v1, v2, tempVec3;
    private FloatArray vertices;
    private Texture checkerboardTexture;

    private boolean enable;
    private boolean mDrawShapeWireframe;

    public DebugRenderer() {
        this(true);
    }

    public DebugRenderer(boolean enable) {
        this.enable = enable;
        batch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1.f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -0.5f, -1.0f, -0.8f));

        v0 = Jolt.New_Vec3();
        v1 = Jolt.New_Vec3();
        v2 = Jolt.New_Vec3();
        tempVec3 = Jolt.New_Vec3();
        vertices = new FloatArray();

        // Create a simple 2x2 checkerboard texture
        Pixmap pixmap = new Pixmap(2, 2, Pixmap.Format.RGBA8888);
        pixmap.setColor(0.3f, 0.3f, 0.3f, 1f);          // Dark gray
        pixmap.fillRectangle(0, 0, 1, 1);         // Top-left
        pixmap.fillRectangle(1, 1, 1, 1);         // Bottom-right
        pixmap.setColor(0.7f, 0.7f, 0.7f, 1f);          // Light gray
        pixmap.fillRectangle(1, 0, 1, 1);         // Top-right
        pixmap.fillRectangle(0, 1, 1, 1);         // Bottom-left
        checkerboardTexture = new Texture(pixmap, true); // Enable mipmapping for smoothness
        checkerboardTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        pixmap.dispose();
    }

    @Override
    protected void DrawMesh(Mat44 inModelMatrix, DebugArrayTriangle triangleArray, Color inModelColor, int inCullMode, int inDrawMode) {
        if(!enable) {
            return;
        }
        int drawMode = inDrawMode == EDrawMode.EDrawMode_Wireframe ? GL20.GL_LINES : GL20.GL_TRIANGLES;
        int size = triangleArray.size();
        if (size == 0) return;

        Vec4 vec4 = inModelColor.ToVec4();
        float r1 = vec4.GetX();
        float g1 = vec4.GetY();
        float b1 = vec4.GetZ();
        float a1 = vec4.GetW();

        int idx = 0;
        for (int i = 0; i < size; i++) {
            DebugRendererTriangle triangle = triangleArray.at(i);
            Color vertexColor = triangle.get_mV(0).get_mColor();
            Vec4 vec41 = vertexColor.ToVec4();
            float r = r1 * vec41.GetX();
            float g = g1 * vec41.GetY();
            float b = b1 * vec41.GetZ();
            float a = a1 * vec41.GetW();

            for (int j = 0; j < 3; j++) {
                DebugRendererVertex mV = triangle.get_mV(j);
                Float3 localPos = mV.get_mPosition();
                tempVec3.Set(localPos.get_x(), localPos.get_y(), localPos.get_z());
                Vec3 worldPos = inModelMatrix.MulVec3(tempVec3);
                float worldPosX = worldPos.GetX();
                float worldPosY = worldPos.GetY();
                float worldPosZ = worldPos.GetZ();
                Float3 localNormal = mV.get_mNormal();
                tempVec3.Set(localNormal.get_x(), localNormal.get_y(), localNormal.get_z());
                Vec3 worldNormal = inModelMatrix.Multiply3x3(tempVec3);
                float worldNormalX = worldNormal.GetX();
                float worldNormalY = worldNormal.GetY();
                float worldNormalZ = worldNormal.GetZ();

                Float2 mUV = mV.get_mUV();
                float u = mUV.get_x();
                float v = mUV.get_y();

                vertices.insert(idx++, worldPosX);
                vertices.insert(idx++, worldPosY);
                vertices.insert(idx++, worldPosZ);
                vertices.insert(idx++, worldNormalX);
                vertices.insert(idx++, worldNormalY);
                vertices.insert(idx++, worldNormalZ);
                vertices.insert(idx++, u);
                vertices.insert(idx++, v);
                vertices.insert(idx++, r);
                vertices.insert(idx++, g);
                vertices.insert(idx++, b);
                vertices.insert(idx++, a);
            }
        }
        ModelProvider modelProvider = obtain();
        modelProvider.setVertices(vertices, drawMode);
        vertices.clear();
        modelRenderer.add(modelProvider);
    }

    private void updateTemp(DebugRendererVertex mV, Mat44 inModelMatrix, Vec3 temp) {
        Float3 mPosition = mV.get_mPosition();
        temp.Set(mPosition.get_x(), mPosition.get_y(), mPosition.get_z());
        Vec3 vv = inModelMatrix.MulVec3(temp);
        temp.Set(vv.GetX(), vv.GetY(), vv.GetZ());
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isEnable() {
        return enable;
    }

    public void begin(Camera camera) {
        batch.begin(camera);
    }

    public void begin(Viewport viewport) {
        begin(viewport.getCamera());
    }

    public void end() {
        for (ModelProvider modelProvider : modelRenderer) {
            batch.render(modelProvider, environment);
        }
        modelPool.addAll(modelRenderer);
        modelRenderer.clear();
        batch.end();
    }

    private ModelProvider obtain() {
        ModelProvider modelProvider;
        if (!modelPool.isEmpty()) {
            int last = modelPool.size() - 1;
            modelProvider = modelPool.get(last);
            modelPool.remove(last);
        } else {
            modelProvider = new ModelProvider(checkerboardTexture);
        }
        return modelProvider;
    }

    @Override
    public void dispose() {
        batch.dispose();
        checkerboardTexture.dispose();
        for (ModelProvider provider : modelPool) {
            provider.dispose();
        }
    }

    @Override
    public void DrawBodies(PhysicsSystem system, BodyManagerDrawSettings inDrawSettings) {
        mDrawShapeWireframe = inDrawSettings.get_mDrawShapeWireframe();
        super.DrawBodies(system, inDrawSettings);
    }

    @Override
    public void DrawBodies(PhysicsSystem system) {
        super.DrawBodies(system);
    }

    @Override
    public void DrawCylinder(Mat44 inMatrix, float inHalfHeight, float inRadius, Color inColor) {
        if(mDrawShapeWireframe) {
            super.DrawCylinder(inMatrix, inHalfHeight, inRadius, inColor, ECastShadow.ECastShadow_Off, EDrawMode.EDrawMode_Wireframe);
        }
        else {
            super.DrawCylinder(inMatrix, inHalfHeight, inRadius, inColor);
        }
    }

    @Override
    public void DrawCylinder(Mat44 inMatrix, float inHalfHeight, float inRadius, Color inColor, int inCastShadow) {
        if(mDrawShapeWireframe) {
            super.DrawCylinder(inMatrix, inHalfHeight, inRadius, inColor, ECastShadow.ECastShadow_Off, EDrawMode.EDrawMode_Wireframe);
        }
        else {
            super.DrawCylinder(inMatrix, inHalfHeight, inRadius, inColor, inCastShadow);
        }
    }

    @Override
    public void DrawCylinder(Mat44 inMatrix, float inHalfHeight, float inRadius, Color inColor, int inCastShadow, int inDrawMode) {
        if(mDrawShapeWireframe) {
            super.DrawCylinder(inMatrix, inHalfHeight, inRadius, inColor, ECastShadow.ECastShadow_Off, EDrawMode.EDrawMode_Wireframe);
        }
        else {
            super.DrawCylinder(inMatrix, inHalfHeight, inRadius, inColor, inCastShadow);
        }
    }

    private void addLineVertex(int idx, Vec3 position, float r, float g, float b, float a) {
        // Position
        vertices.insert(idx++, position.GetX());
        vertices.insert(idx++, position.GetY());
        vertices.insert(idx++, position.GetZ());
        // Normal (using a default normal since lines don't typically need them)
        vertices.insert(idx++, 0f);
        vertices.insert(idx++, 1f);
        vertices.insert(idx++, 0f);
        // UV coordinates (using default values)
        vertices.insert(idx++, 0f);
        vertices.insert(idx++, 0f);
        // Color
        vertices.insert(idx++, r);
        vertices.insert(idx++, g);
        vertices.insert(idx++, b);
        vertices.insert(idx++, a);
    }

    private class ModelProvider implements RenderableProvider {
        public final Mesh mesh;
        public final MeshPart meshPart;
        public Model model;
        public final Matrix4 transform = new Matrix4();
        private final Material material;

        public ModelProvider(Texture texture) {
            material = new Material(TextureAttribute.createDiffuse(texture));
            mesh = new Mesh(true, 99999, 0,
                    new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"),
                    new VertexAttribute(VertexAttributes.Usage.Normal, 3, "a_normal"),
                    new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, "a_texCoord0"),
                    new VertexAttribute(VertexAttributes.Usage.ColorUnpacked, 4, "a_color"));
            meshPart = new MeshPart("meshpart1", mesh, 0, 0, GL20.GL_TRIANGLES);
            Node node = new Node();
            node.id = "node1";
            node.parts.add(new NodePart(meshPart, material));
            model = new Model();
            model.nodes.add(node);
            model.meshes.add(mesh);
            model.materials.add(material);
            model.meshParts.add(meshPart);
        }

        public void setVertices(FloatArray vertices, int primitiveType) {
            mesh.setVertices(vertices.items, 0, vertices.size);
            int floatsPerVertex = mesh.getVertexSize() / 4; // 8 floats: 3 pos + 3 norm + 2 tex
            meshPart.size = vertices.size / floatsPerVertex;
            meshPart.primitiveType = primitiveType;
        }

        @Override
        public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
            Renderable renderable = pool.obtain();
            Node node = model.nodes.get(0);
            NodePart nodePart = node.parts.get(0);
            nodePart.setRenderable(renderable);
            if (nodePart.bones == null && this.transform != null) {
                renderable.worldTransform.set(this.transform).mul(node.globalTransform);
            } else if (this.transform != null) {
                renderable.worldTransform.set(this.transform);
            } else {
                renderable.worldTransform.idt();
            }
            renderable.environment = environment;
            renderables.add(renderable);
        }

        public void dispose() {
            model.dispose();
        }
    }
}
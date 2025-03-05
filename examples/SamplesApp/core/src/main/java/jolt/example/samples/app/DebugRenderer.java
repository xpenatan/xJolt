package jolt.example.samples.app;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.ArrayList;
import jolt.DebugArrayTriangle;
import jolt.DebugRendererEm;
import jolt.DebugRendererTriangle;
import jolt.DebugRendererVertex;
import jolt.EDrawMode;
import jolt.jolt.core.Color;
import jolt.jolt.math.Float2;
import jolt.jolt.math.Float3;
import jolt.jolt.math.Mat44;
import jolt.jolt.math.Vec3;
import jolt.jolt.math.Vec4;

public class DebugRenderer extends DebugRendererEm {

    private ModelBatch batch;
    protected Environment environment;

    private ShapeRenderer filledShapeRenderer;
    private ShapeRenderer lineShapeRenderer;
    private SpriteBatch spriteBatch;
    private com.badlogic.gdx.graphics.Color color;
    private ArrayList<ModelProvider> modelPool = new ArrayList<>();
    private ArrayList<ModelProvider> modelRenderer = new ArrayList<>();

    private Vec3 v0, v1, v2, tempVec3;
    private FloatArray vertices;
    private Texture checkerboardTexture;

    private boolean enable;

    public DebugRenderer() {
        this(true);
    }

    public DebugRenderer(boolean enable) {
        this.enable = enable;
        batch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1.f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -0.5f, -1.0f, -0.8f));

        spriteBatch = new SpriteBatch();
        filledShapeRenderer = new ShapeRenderer();
        lineShapeRenderer = new ShapeRenderer();
        color = new com.badlogic.gdx.graphics.Color();

        v0 = new Vec3();
        v1 = new Vec3();
        v2 = new Vec3();
        tempVec3 = new Vec3();
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
        boolean wireframeMode = inDrawMode == EDrawMode.EDrawMode_Wireframe;
        boolean solidMode = inDrawMode == EDrawMode.EDrawMode_Solid;
        int size = triangleArray.size();
        if (size == 0) return;

        Vec4 vec4 = inModelColor.ToVec4();
        float r1 = vec4.GetX();
        float g1 = vec4.GetY();
        float b1 = vec4.GetZ();
        float a1 = vec4.GetW();

        if (wireframeMode) {
            for (int i = 0; i < size; i++) {
                DebugRendererTriangle triangle = triangleArray.at(i);
                Color vertexColor = triangle.get_mV(0).get_mColor();
                Vec4 vec41 = vertexColor.ToVec4();
                float r = r1 * vec41.GetX();
                float g = g1 * vec41.GetY();
                float b = b1 * vec41.GetZ();
                float a = a1 * vec41.GetW();

                color.set(r, g, b, a);
                lineShapeRenderer.setColor(color);

                DebugRendererVertex mV0 = triangle.get_mV(0);
                updateTemp(mV0, inModelMatrix, v0);
                DebugRendererVertex mV1 = triangle.get_mV(1);
                updateTemp(mV1, inModelMatrix, v1);
                DebugRendererVertex mV2 = triangle.get_mV(2);
                updateTemp(mV2, inModelMatrix, v2);
                lineShapeRenderer.line(v0.GetX(), v0.GetY(), v0.GetZ(), v1.GetX(), v1.GetY(), v1.GetZ());
                lineShapeRenderer.line(v1.GetX(), v1.GetY(), v1.GetZ(), v2.GetX(), v2.GetY(), v2.GetZ());
                lineShapeRenderer.line(v2.GetX(), v2.GetY(), v2.GetZ(), v0.GetX(), v0.GetY(), v0.GetZ());
            }
        } else if (solidMode) {
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
            modelProvider.setVertices(vertices, GL20.GL_TRIANGLES);
            vertices.clear();
            modelRenderer.add(modelProvider);
        }
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
        filledShapeRenderer.setProjectionMatrix(camera.combined);
        filledShapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        lineShapeRenderer.setProjectionMatrix(camera.combined);
        lineShapeRenderer.begin(ShapeRenderer.ShapeType.Line);
    }

    public void begin(Viewport viewport) {
        begin(viewport.getCamera());
    }

    public void end() {
        filledShapeRenderer.end();
        lineShapeRenderer.end();
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
        spriteBatch.dispose();
        filledShapeRenderer.dispose();
        lineShapeRenderer.dispose();
        checkerboardTexture.dispose();
        for (ModelProvider provider : modelPool) {
            provider.dispose();
        }
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
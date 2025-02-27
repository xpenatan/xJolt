package jolt.example.samples.app;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.ArrayList;
import jolt.ArrayTriangle;
import jolt.DebugRendererEm;
import jolt.DebugRendererTriangle;
import jolt.DebugRendererVertex;
import jolt.EDrawMode;
import jolt.jolt.core.Color;
import jolt.jolt.math.Float3;
import jolt.jolt.math.Mat44;
import jolt.jolt.math.Vec3;

public class DebugRenderer extends DebugRendererEm {

    private Camera camera;
    private ShapeRenderer filledShapeRenderer;
    private ShapeRenderer lineShapeRenderer;
    private SpriteBatch spriteBatch;
    private com.badlogic.gdx.graphics.Color color;
    ModelBatch batch;
    private ArrayList<Model> modelsToDispose = new ArrayList<>();

    private Vec3 v0;
    private Vec3 v1;
    private Vec3 v2;

    public DebugRenderer() {
        batch = new ModelBatch();
        spriteBatch = new SpriteBatch();
        filledShapeRenderer = new ShapeRenderer();
        lineShapeRenderer = new ShapeRenderer();
        color = new com.badlogic.gdx.graphics.Color();

        color = new com.badlogic.gdx.graphics.Color();
        v0 = new Vec3();
        v1 = new Vec3();
        v2 = new Vec3();
    }

    @Override
    protected void DrawMesh(Mat44 inModelMatrix, ArrayTriangle triangleArray, Color inModelColor, int inCullMode, int inDrawMode) {
        boolean wireframeMode = inDrawMode == EDrawMode.EDrawMode_Wireframe;
        boolean solidMode = inDrawMode == EDrawMode.EDrawMode_Solid;
        int size = triangleArray.size();
        if (size == 0) return;

        float[] vertices;
        int primitiveType;
        int vertexCount;
        if (wireframeMode) {
            vertices = new float[6 * size * 3];
            int idx = 0;
            for (int i = 0; i < size; i++) {
                DebugRendererTriangle triangle = triangleArray.at(i);
                DebugRendererVertex mV0 = triangle.get_mV(0);
                updateTemp(mV0, inModelMatrix, v0);
                DebugRendererVertex mV1 = triangle.get_mV(1);
                updateTemp(mV1, inModelMatrix, v1);
                DebugRendererVertex mV2 = triangle.get_mV(2);
                updateTemp(mV2, inModelMatrix, v2);

                vertices[idx++] = v0.GetX();
                vertices[idx++] = v0.GetY();
                vertices[idx++] = v0.GetZ();
                vertices[idx++] = v1.GetX();
                vertices[idx++] = v1.GetY();
                vertices[idx++] = v1.GetZ();
                vertices[idx++] = v1.GetX();
                vertices[idx++] = v1.GetY();
                vertices[idx++] = v1.GetZ();
                vertices[idx++] = v2.GetX();
                vertices[idx++] = v2.GetY();
                vertices[idx++] = v2.GetZ();
                vertices[idx++] = v2.GetX();
                vertices[idx++] = v2.GetY();
                vertices[idx++] = v2.GetZ();
                vertices[idx++] = v0.GetX();
                vertices[idx++] = v0.GetY();
                vertices[idx++] = v0.GetZ();
            }
            primitiveType = GL20.GL_LINES;
            vertexCount = 6 * size;
        } else if (solidMode) {
            vertices = new float[3 * size * 3];
            int idx = 0;
            for (int i = 0; i < size; i++) {
                DebugRendererTriangle triangle = triangleArray.at(i);
                DebugRendererVertex mV0 = triangle.get_mV(0);
                updateTemp(mV0, inModelMatrix, v0);
                DebugRendererVertex mV1 = triangle.get_mV(1);
                updateTemp(mV1, inModelMatrix, v1);
                DebugRendererVertex mV2 = triangle.get_mV(2);
                updateTemp(mV2, inModelMatrix, v2);
                vertices[idx++] = v0.GetX();
                vertices[idx++] = v0.GetY();
                vertices[idx++] = v0.GetZ();
                vertices[idx++] = v1.GetX();
                vertices[idx++] = v1.GetY();
                vertices[idx++] = v1.GetZ();
                vertices[idx++] = v2.GetX();
                vertices[idx++] = v2.GetY();
                vertices[idx++] = v2.GetZ();
            }
            primitiveType = GL20.GL_TRIANGLES;
            vertexCount = 3 * size;
        } else {
            return;
        }

        Mesh mesh = new Mesh(true, vertexCount, 0, new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"));
        mesh.setVertices(vertices);

        com.badlogic.gdx.graphics.Color libgdxColor = new com.badlogic.gdx.graphics.Color(inModelColor.get_mU32());
        Material material = new Material(ColorAttribute.createDiffuse(libgdxColor));

        MeshPart meshPart = new MeshPart("meshpart1", mesh, 0, vertexCount, primitiveType);
        Node node = new Node();
        node.id = "node1";
        node.parts.add(new NodePart(meshPart, material));
        Model model = new Model();
        model.nodes.add(node);
        model.meshes.add(mesh);
        model.materials.add(material);
        model.meshParts.add(meshPart);

        ModelInstance instance = new ModelInstance(model);
//        Matrix4 transform = new Matrix4(inModelMatrix.getValues());
        Matrix4 transform = new Matrix4();
        instance.transform.set(transform);

        batch.render(instance);
        modelsToDispose.add(model);
    }

    private void updateTemp(DebugRendererVertex mV0, Mat44 inModelMatrix, Vec3 temp0) {
        Float3 mPosition0 = mV0.get_mPosition();
        float x = mPosition0.get_x();
        float y = mPosition0.get_y();
        float z = mPosition0.get_z();
        temp0.Set(x, y, z);
        Vec3 vv0 = inModelMatrix.Multiply(temp0);
        float v0X = vv0.GetX();
        float v0Y = vv0.GetY();
        float v0Z = vv0.GetZ();
        temp0.Set(v0X, v0Y, v0Z);
    }

    @Override
    protected void DrawLine(Vec3 inFrom, Vec3 inTo, Color inColor) {
        if (camera != null) {
            float fromX = inFrom.GetX();
            float fromY = inFrom.GetY();
            float fromZ = inFrom.GetZ();
            float toX = inTo.GetX();
            float toY = inTo.GetY();
            float toZ = inTo.GetZ();
            int mU32 = inColor.get_mU32();
            color.set(mU32);
            lineShapeRenderer.setColor(color);
            lineShapeRenderer.line(fromX, fromY, fromZ, toX, toY, toZ);
        }
    }

    @Override
    protected void DrawTriangle(Vec3 inV1, Vec3 inV2, Vec3 inV3, Color inColor, int inCastShadow) {
        if (camera != null) {
            float x1 = inV1.GetX();
            float y1 = inV1.GetY();
            float z1 = inV1.GetZ();
            float x2 = inV2.GetX();
            float y2 = inV2.GetY();
            float z2 = inV2.GetZ();
            float x3 = inV3.GetX();
            float y3 = inV3.GetY();
            float z3 = inV3.GetZ();
            color.set(inColor.get_mU32());
            filledShapeRenderer.setColor(color);
            triangle3D(x1, y1, z1, x2, y2, z2, x3, y3, z3);
        }
    }

    @Override
    protected void DrawText3D(Vec3 inPosition, long inString, int inStringLen, Color inColor, float inHeight) {
        if (camera != null) {
            System.out.println("DrawText3D");
        }
    }

    public void begin(Camera camera) {
        this.camera = camera;
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
        batch.end();
        for (Model m : modelsToDispose) {
            m.dispose();
        }
        modelsToDispose.clear();
        camera = null;
    }

    public void dispose() {
        super.dispose();
        filledShapeRenderer.dispose();
        lineShapeRenderer.dispose();
        spriteBatch.dispose();
        batch.dispose();
    }

    public void triangle3D(float x1, float y1, float z1,
                           float x2, float y2, float z2,
                           float x3, float y3, float z3) {
        ImmediateModeRenderer renderer = filledShapeRenderer.getRenderer();
        float colorBits = color.toFloatBits();
        renderer.color(colorBits);
        renderer.vertex(x1, y1, z1);
        renderer.color(colorBits);
        renderer.vertex(x2, y2, z2);
        renderer.color(colorBits);
        renderer.vertex(x3, y3, z3);
    }
}
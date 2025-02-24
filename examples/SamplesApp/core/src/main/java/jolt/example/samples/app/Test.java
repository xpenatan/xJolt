package jolt.example.samples.app;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import jolt.DebugRendererSimpleEm;
import jolt.EMotionType;
import jolt.JoltInterface;
import jolt.JoltSettings;
import jolt.MeshShapeSettings;
import jolt.PhysicsMaterialList;
import jolt.RVec3;
import jolt.ShapeResult;
import jolt.TriangleList;
import jolt.jolt.core.TempAllocator;
import jolt.jolt.geometry.Triangle;
import jolt.jolt.math.Quat;
import jolt.jolt.math.Vec3;
import jolt.jolt.physics.PhysicsSystem;
import jolt.jolt.physics.body.Body;
import jolt.jolt.physics.body.BodyCreationSettings;
import jolt.jolt.physics.body.BodyInterface;
import jolt.jolt.physics.collision.shape.BoxShape;
import static jolt.EMotionType.EMotionType_Static;
import static jolt.jolt.physics.EActivation.EActivation_Activate;
import static jolt.jolt.physics.EActivation.EActivation_DontActivate;

public abstract class Test {


    protected PhysicsSystem physicsSystem = null;
    protected BodyInterface bodyInterface = null;

    public void setPhysicsSystem(PhysicsSystem physicsSystem) {
        this.physicsSystem = physicsSystem;
        bodyInterface = physicsSystem.GetBodyInterface();
    }

    public void dispose() {

    }

    public void update(float delta) {
    }

    protected void initialize() {}

    protected float getWorldScale() { return 1.0f; }

    protected Body createFloor() {
        return createFloor(200f);
    }

    protected Body createFloor(float inSize) {
        float scale = getWorldScale();
        Vec3 inHalfExtent = new Vec3(scale * (0.5f * inSize), scale * 1.0f, scale * (0.5f * inSize));
        RVec3 inPosition = new RVec3(0.0f, scale * -1.0f, 0.0f);
        Quat inRotation = Quat.sIdentity();
        BoxShape bodyShape = new BoxShape(inHalfExtent, 0.0f);
        BodyCreationSettings bodySettings = new BodyCreationSettings(bodyShape, inPosition, inRotation, EMotionType_Static, SamplesApp.LAYER_NON_MOVING);
        Body body = bodyInterface.CreateBody(bodySettings);
        bodyInterface.AddBody(body.GetID(), EActivation_DontActivate);
        inHalfExtent.dispose();
        inPosition.dispose();
        return body;
    }

    private float height(float x, float y) {
        return MathUtils.sin(x / 2) * MathUtils.cos(y / 3);
    }

    private void createMeshFloor(int n,  int cellSize, int maxHeight, float posX, float posY, float posZ) {
        // Create regular grid of triangles

        TriangleList triangles = new TriangleList();
        triangles.resize(n * n * 2);
        for (int x = 0; x < n; ++x)
            for (int z = 0; z < n; ++z) {
                float center = n * cellSize / 2.0f;

                float x1 = cellSize * x - center;
                float z1 = cellSize * z - center;
                float x2 = x1 + cellSize;
                float z2 = z1 + cellSize;

                {
                    Triangle t = triangles.at((x * n + z) * 2);
                    var v1 = t.get_mV(0);
                    v1.set_x(x1);
                    v1.set_y(height(x, z));
                    v1.set_z(z1);
                    var v2 = t.get_mV(1);
                    v2.set_x(x1);
                    v2.set_y(height(x, z + 1));
                    v2.set_z(z2);
                    var v3 = t.get_mV(2);
                    v3.set_x(x2);
                    v3.set_y(height(x + 1, z + 1));
                    v3.set_z(z2);
                }

                {
                    var t = triangles.at((x * n + z) * 2 + 1);
                    var v1 = t.get_mV(0);
                    v1.set_x(x1);
                    v1.set_y(height(x, z));
                    v1.set_z(z1);
                    var v2 = t.get_mV(1);
                    v2.set_x(x2);
                    v2.set_y(height(x + 1, z + 1));
                    v2.set_z(z2);
                    var v3 = t.get_mV(2);
                    v3.set_x(x2);
                    v3.set_y(height(x + 1, z));
                    v3.set_z(z1);
                }
            }
        var materials = new PhysicsMaterialList();
        ShapeResult shapeResult = new MeshShapeSettings(triangles, materials).Create();
        triangles.dispose();
        materials.dispose();
        boolean hasError = shapeResult.HasError();
        boolean isValid = shapeResult.IsValid();
        System.out.println("ShapeResult hasError: " + hasError);
        System.out.println("ShapeResult isValid: " + isValid);
//        IDLString idlString = shapeResult.GetError();
//        long cPointer = idlString.getCPointer();
//        String data = idlString.data();
//        System.out.println("ShapeResult GetError: " + data);
        var shape = shapeResult.Get();
        // Create body
        var creationSettings = new BodyCreationSettings(shape, new RVec3(posX, posY, posZ), new Quat(0, 0, 0, 1), EMotionType_Static, SamplesApp.LAYER_NON_MOVING);
        var body = bodyInterface.CreateBody(creationSettings);
        creationSettings.dispose();
        addToScene(body, 0xc7c7c7);
    }

    void addToScene(Body body, int color) {
        bodyInterface.AddBody(body.GetID(), EActivation_Activate);

//        addToThreeScene(body, color);
    }

}
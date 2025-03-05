package jolt.example.samples.app.tests;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import jolt.example.samples.app.Layers;
import jolt.example.samples.app.Test;
import jolt.jolt.Jolt;
import jolt.jolt.math.Quat;
import jolt.jolt.math.Vec3;
import jolt.jolt.physics.body.Body;
import jolt.jolt.physics.body.BodyCreationSettings;
import jolt.jolt.physics.collision.shape.BoxShape;
import static jolt.EMotionType.EMotionType_Dynamic;
import static jolt.jolt.physics.EActivation.EActivation_Activate;

public class BoxShapeTest extends Test {

    public static float JPH_PI = 3.14159265358979323846f;

    @Override
    protected void initialize() {
        // Floor
        createFloor();

        Body body1 = createBody(new Vector3(20, 1, 1), new Vector3(0, 10, 0), Quat.sIdentity());
        mBodyInterface.AddBody(body1.GetID(), EActivation_Activate);

        Body body2 = createBody(new Vector3(2, 3, 4), new Vector3(0, 10, 10), Quat.sRotation(Vec3.sAxisZ(), 0.25f * JPH_PI));
        mBodyInterface.AddBody(body2.GetID(), EActivation_Activate);

        // Methods that return a Value c++ object will be replaced every time its called. Save its value before calling again.
        Quat quatX3 = Quat.sRotation(Vec3.sAxisX(), 0.25f * JPH_PI);
        float q1X = quatX3.GetX();
        float q1Y = quatX3.GetY();
        float q1Z = quatX3.GetZ();
        float q1W = quatX3.GetW();
        Quat quatZ3 = Quat.sRotation(Vec3.sAxisZ(), 0.25f * JPH_PI);
        float q2X = quatZ3.GetX();
        float q2Y = quatZ3.GetY();
        float q2Z = quatZ3.GetZ();
        float q2W = quatZ3.GetW();
        Quaternion q1 = new Quaternion(q1X, q1Y, q1Z, q1W);
        Quaternion q2 = new Quaternion(q2X, q2Y, q2Z, q2W);
        Quaternion mul = q1.mul(q2);
        quatX3.SetX(mul.x);
        quatX3.SetY(mul.y);
        quatX3.SetZ(mul.z);
        quatX3.SetW(mul.w);
        Body body3 = createBody(new Vector3(0.5f, 0.75f, 1.0f), new Vector3(0, 10, 20), quatX3);
        mBodyInterface.AddBody(body3.GetID(), EActivation_Activate);
    }

    private Body createBody(Vector3 inHalfExtent, Vector3 inPosition, Quat inRotation) {
        float scale = getWorldScale();
        Vec3 inHalfExtentJolt = new Vec3(inHalfExtent.x, inHalfExtent.y, inHalfExtent.z);
        Vec3 inPositionJolt = new Vec3(inPosition.x, inPosition.y, inPosition.z);
        BoxShape bodyShape = new BoxShape(inHalfExtentJolt);
        BodyCreationSettings bodySettings = Jolt.BodyCreationSettings_New(bodyShape, inPositionJolt, inRotation, EMotionType_Dynamic, Layers.MOVING);
        Body body = mBodyInterface.CreateBody(bodySettings);
        inHalfExtentJolt.dispose();
        inPositionJolt.dispose();
        return body;
    }
}
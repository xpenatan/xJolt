package jolt.example.samples.app.tests;

import com.badlogic.gdx.math.Vector3;
import jolt.RVec3;
import jolt.example.samples.app.SamplesApp;
import jolt.example.samples.app.Test;
import jolt.jolt.math.Quat;
import jolt.jolt.math.Vec3;
import jolt.jolt.physics.body.Body;
import jolt.jolt.physics.body.BodyCreationSettings;
import jolt.jolt.physics.collision.shape.BoxShape;
import static jolt.EMotionType.EMotionType_Dynamic;
import static jolt.jolt.physics.EActivation.EActivation_Activate;

public class BoxShapeTest extends Test {

    static float JPH_PI = 3.14159265358979323846f;

    @Override
    protected void initialize() {
        // Floor
        createFloor();

        Body body1 = createBody(new Vector3(20, 1, 1), new Vector3(0, 10, 0), Quat.sIdentity());
        bodyInterface.AddBody(body1.GetID(), EActivation_Activate);

        Body body2 = createBody(new Vector3(2, 3, 4), new Vector3(0, 10, 10), Quat.sRotation(Vec3.sAxisZ(), 0.25f * JPH_PI));
        bodyInterface.AddBody(body2.GetID(), EActivation_Activate);

        //TODO figure out why this CreateBody crash when calling mBodyManager->AllocateBody(inSettings);
//        Quat quatX3 = Quat.sRotation(Vec3.sAxisX(), 0.25f * JPH_PI);
//        Quat quatZ3 = Quat.sRotation(Vec3.sAxisZ(), 0.25f * JPH_PI);
//        quatX3.SetX(quatX3.GetX() * quatZ3.GetX());
//        quatX3.SetY(quatX3.GetY() * quatZ3.GetY());
//        quatX3.SetZ(quatX3.GetZ() * quatZ3.GetZ());
//        Body body3 = createBody(new Vector3(0.5f, 0.75f, 1.0f), new Vector3(0, 10, 20), quatX3);
//        bodyInterface.AddBody(body3.GetID(), EActivation_Activate);
    }

    private Body createBody(Vector3 inHalfExtent, Vector3 inPosition, Quat inRotation) {
        float scale = getWorldScale();
        Vec3 inHalfExtentJolt = new Vec3(inHalfExtent.x, inHalfExtent.y, inHalfExtent.z);
        RVec3 inPositionJolt = new RVec3(inPosition.x, inPosition.y, inPosition.z);
        BoxShape bodyShape = new BoxShape(inHalfExtentJolt);
        BodyCreationSettings bodySettings = new BodyCreationSettings(bodyShape, inPositionJolt, inRotation, EMotionType_Dynamic, SamplesApp.LAYER_MOVING);
        Body body = bodyInterface.CreateBody(bodySettings);
        inHalfExtentJolt.dispose();
        inPositionJolt.dispose();
        return body;
    }
}
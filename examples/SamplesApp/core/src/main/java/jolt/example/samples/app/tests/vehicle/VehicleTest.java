package jolt.example.samples.app.tests.vehicle;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import jolt.ArrayVec3;
import jolt.ConvexHullShapeSettings;
import jolt.EMotionType;
import jolt.example.samples.app.Layers;
import jolt.example.samples.app.Test;
import jolt.jolt.Jolt;
import jolt.jolt.math.Quat;
import jolt.jolt.math.Vec3;
import jolt.jolt.physics.EActivation;
import jolt.jolt.physics.body.Body;
import jolt.jolt.physics.body.BodyCreationSettings;
import jolt.jolt.physics.collision.shape.BoxShape;
import jolt.jolt.physics.collision.shape.Shape;
import static jolt.example.samples.app.tests.BoxShapeTest.JPH_PI;

public abstract class VehicleTest extends Test {

    @Override
    protected void initialize() {
        // Scene with hilly terrain and some objects to drive into
        Body floor = CreateMeshTerrain();
        floor.SetFriction(1.0f);

        createBridge();

        createWall();

        createRubble();
    }

    private void createBridge() {

    }

    private void createWall() {
        Shape box_shape = new BoxShape(new Vec3(0.5f, 0.5f, 0.5f));
        for (int i = 0; i < 3; ++i) {
            for (int j = i / 2; j < 5 - (i + 1) / 2; ++j)
            {
                Vec3 position = new Vec3(2.0f + j * 1.0f + ((i & 1) == 1 ? 0.5f : 0.0f), 2.0f + i * 1.0f, 10.0f);
                mBodyInterface.CreateAndAddBody(Jolt.BodyCreationSettings_New(box_shape, position, Quat.sIdentity(), EMotionType.EMotionType_Dynamic, Layers.MOVING), EActivation.EActivation_Activate);
            }
        }
    }

    private void createRubble() {
        // Flat and light objects
        Shape box_shape = new BoxShape(new Vec3(0.5f, 0.1f, 0.5f));
        for (int i = 0; i < 5; ++i)
            for (int j = 0; j < 5; ++j)
            {
                Vec3 position = new Vec3(-5.0f + j, 2.0f + i * 0.2f, 10.0f + 0.5f * i);
                mBodyInterface.CreateAndAddBody(Jolt.BodyCreationSettings_New(box_shape, position, Quat.sIdentity(), EMotionType.EMotionType_Dynamic, Layers.MOVING), EActivation.EActivation_Activate);
            }


        // Light convex shapes
        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 10; ++j) {
                // Create random points
                ArrayVec3 points = new ArrayVec3();
                for (int k = 0; k < 20; ++k) {
                    float value = MathUtils.random(0.2f, 0.4f);
                    float theta = JPH_PI * MathUtils.random(0.0f, 1.0f);
                    float phi = 2.0f * JPH_PI * MathUtils.random(0.0f, 1.0f);
                    Vec3 vec3 = Vec3.sUnitSpherical(theta, phi);
                    Vec3 vec = new Vec3(vec3.GetX() * value, vec3.GetY() * value, vec3.GetZ() * value);
                    points.push_back(vec);
                }

                mBodyInterface.CreateAndAddBody(Jolt.BodyCreationSettings_New(new ConvexHullShapeSettings(points), new Vec3(-5.0f + 0.5f * j, 2.0f, 15.0f + 0.5f * i), Quat.sIdentity(), EMotionType.EMotionType_Dynamic, Layers.MOVING), EActivation.EActivation_Activate);
            }
        }
    }
}
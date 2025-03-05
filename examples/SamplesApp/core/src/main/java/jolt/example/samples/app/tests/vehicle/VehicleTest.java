package jolt.example.samples.app.tests.vehicle;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import jolt.ArrayVec3;
import jolt.ConvexHullShapeSettings;
import jolt.DistanceConstraintSettings;
import jolt.EMotionType;
import jolt.example.samples.app.Layers;
import jolt.example.samples.app.Test;
import jolt.jolt.Jolt;
import jolt.jolt.math.Quat;
import jolt.jolt.math.Vec3;
import jolt.jolt.physics.EActivation;
import jolt.jolt.physics.body.Body;
import jolt.jolt.physics.body.BodyCreationSettings;
import jolt.jolt.physics.collision.CollisionGroup;
import jolt.jolt.physics.collision.GroupFilterTable;
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
        int cChainLength = 20;

        // Build a collision group filter that disables collision between adjacent bodies
        GroupFilterTable group_filter = new GroupFilterTable(cChainLength);
        for (int i = 0; i < cChainLength - 1; ++i) {
            group_filter.DisableCollision(i, i + 1);
        }

        Vec3 part_half_size = Jolt.New_Vec3(2.5f, 0.25f, 1.0f);
        Shape part_shape = new BoxShape(part_half_size);

        Vec3 large_part_half_size = Jolt.New_Vec3(2.5f, 0.25f, 22.5f);
        Shape large_part_shape = new BoxShape(large_part_half_size);

        Quat first_part_rot = Quat.sRotation(Vec3.sAxisX(), MathUtils.degreesToRadians * -10.0f);

        Vec3 prev_pos = Jolt.New_Vec3(-25, 7, 0);
        Body prev_part = null;

        for (int i = 0; i < cChainLength; ++i) {
            float prevX = prev_pos.GetX();
            float prevY = prev_pos.GetY();
            float prevZ = prev_pos.GetZ();

            Vec3 pos = Jolt.New_Vec3(prevX, prevY, (2.0f * part_half_size.GetZ()) + prevZ);
            Vec3 val = Jolt.New_Vec3(0, large_part_half_size.GetY() - part_half_size.GetY(), large_part_half_size.GetZ() - part_half_size.GetZ());
            Body part = i == 0 ? mBodyInterface.CreateBody(Jolt.New_BodyCreationSettings(large_part_shape, pos.SubVec3(first_part_rot.MulVec3(val)), first_part_rot, EMotionType.EMotionType_Static, Layers.NON_MOVING))
                    : mBodyInterface.CreateBody(Jolt.New_BodyCreationSettings(part_shape, pos, Quat.sIdentity(), i == 19? EMotionType.EMotionType_Static : EMotionType.EMotionType_Dynamic, i == 19? Layers.NON_MOVING : Layers.MOVING));
            part.SetCollisionGroup(new CollisionGroup(group_filter, 1, i));
            part.SetFriction(1.0f);
            mBodyInterface.AddBody(part.GetID(), EActivation.EActivation_Activate);

            if (prev_part != null) {
                DistanceConstraintSettings dc = new DistanceConstraintSettings();
                dc.set_mPoint1(prev_pos.Add(Jolt.New_Vec3(-part_half_size.GetX(), 0, part_half_size.GetZ())));
                dc.set_mPoint2(pos.Add(Jolt.New_Vec3(-part_half_size.GetX(), 0, -part_half_size.GetZ())));
                mPhysicsSystem.AddConstraint(dc.Create(prev_part, part));

                dc.set_mPoint1(prev_pos.Add(Jolt.New_Vec3(part_half_size.GetX(), 0, part_half_size.GetZ())));
                dc.set_mPoint2(pos.Add(Jolt.New_Vec3(part_half_size.GetX(), 0, -part_half_size.GetZ())));
                mPhysicsSystem.AddConstraint(dc.Create(prev_part, part));
            }

            prev_part = part;
            prev_pos = pos;
        }
    }

    private void createWall() {
        Shape box_shape = new BoxShape(Jolt.New_Vec3(0.5f, 0.5f, 0.5f));
        for (int i = 0; i < 3; ++i) {
            for (int j = i / 2; j < 5 - (i + 1) / 2; ++j)
            {
                Vec3 position = Jolt.New_Vec3(2.0f + j * 1.0f + ((i & 1) == 1 ? 0.5f : 0.0f), 2.0f + i * 1.0f, 10.0f);
                mBodyInterface.CreateAndAddBody(Jolt.New_BodyCreationSettings(box_shape, position, Quat.sIdentity(), EMotionType.EMotionType_Dynamic, Layers.MOVING), EActivation.EActivation_Activate);
            }
        }
    }

    private void createRubble() {
        // Flat and light objects
        Shape box_shape = new BoxShape(Jolt.New_Vec3(0.5f, 0.1f, 0.5f));
        for (int i = 0; i < 5; ++i)
            for (int j = 0; j < 5; ++j)
            {
                Vec3 position = Jolt.New_Vec3(-5.0f + j, 2.0f + i * 0.2f, 10.0f + 0.5f * i);
                mBodyInterface.CreateAndAddBody(Jolt.New_BodyCreationSettings(box_shape, position, Quat.sIdentity(), EMotionType.EMotionType_Dynamic, Layers.MOVING), EActivation.EActivation_Activate);
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
                    Vec3 vec = Jolt.New_Vec3(vec3.GetX() * value, vec3.GetY() * value, vec3.GetZ() * value);
                    points.push_back(vec);
                }

                mBodyInterface.CreateAndAddBody(Jolt.New_BodyCreationSettings(new ConvexHullShapeSettings(points), Jolt.New_Vec3(-5.0f + 0.5f * j, 2.0f, 15.0f + 0.5f * i), Quat.sIdentity(), EMotionType.EMotionType_Dynamic, Layers.MOVING), EActivation.EActivation_Activate);
            }
        }
    }
}
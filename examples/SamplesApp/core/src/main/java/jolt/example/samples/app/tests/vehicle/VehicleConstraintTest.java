package jolt.example.samples.app.tests.vehicle;

import com.badlogic.gdx.math.MathUtils;
import jolt.ArrayVehicleAntiRollBar;
import jolt.ArrayWheelSettings;
import jolt.EMotionType;
import jolt.EOverrideMassProperties;
import jolt.OffsetCenterOfMassShapeSettings;
import jolt.VehicleCollisionTesterCastCylinder;
import jolt.VehicleCollisionTesterCastSphere;
import jolt.VehicleCollisionTesterRay;
import jolt.VehicleConstraintSettings;
import jolt.WheelSettingsWV;
import jolt.WheeledVehicleControllerSettings;
import jolt.example.samples.app.Layers;
import jolt.jolt.math.Mat44;
import jolt.jolt.math.Quat;
import jolt.jolt.math.Vec3;
import jolt.jolt.physics.EActivation;
import jolt.jolt.physics.body.Body;
import jolt.jolt.physics.body.BodyCreationSettings;
import jolt.jolt.physics.collision.shape.BoxShape;
import jolt.jolt.physics.collision.shape.Shape;
import jolt.jolt.physics.vehicle.VehicleCollisionTester;
import jolt.jolt.physics.vehicle.VehicleConstraint;

public class VehicleConstraintTest extends VehicleTest {

    private static float sInitialRollAngle = 0;
    private static float sMaxRollAngle = MathUtils.degreesToRadians * 60.0f;
    private static float sMaxSteeringAngle = MathUtils.degreesToRadians * 30.0f;
    private static int sCollisionMode = 2;
    private static boolean sFourWheelDrive = false;
    private static boolean sAntiRollbar = true;
    private static boolean sLimitedSlipDifferentials = true;
    private static boolean sOverrideGravity = false;					///< If true, gravity is overridden to always oppose the ground normal
    private static float sMaxEngineTorque = 500.0f;
    private static float sClutchStrength = 10.0f;
    private static float sFrontCasterAngle = 0.0f;
    private static float sFrontKingPinAngle = 0.0f;
    private static float sFrontCamber = 0.0f;
    private static float sFrontToe = 0.0f;
    private static float sFrontSuspensionForwardAngle = 0.0f;
    private static float sFrontSuspensionSidewaysAngle = 0.0f;
    private static float sFrontSuspensionMinLength = 0.3f;
    private static float sFrontSuspensionMaxLength = 0.5f;
    private static float sFrontSuspensionFrequency = 1.5f;
    private static float sFrontSuspensionDamping = 0.5f;
    private static float sRearSuspensionForwardAngle = 0.0f;
    private static float sRearSuspensionSidewaysAngle = 0.0f;
    private static float sRearCasterAngle = 0.0f;
    private static float sRearKingPinAngle = 0.0f;
    private static float sRearCamber = 0.0f;
    private static float sRearToe = 0.0f;
    private static float sRearSuspensionMinLength = 0.3f;
    private static float sRearSuspensionMaxLength = 0.5f;
    private static float sRearSuspensionFrequency = 1.5f;
    private static float sRearSuspensionDamping = 0.5f;

    private Body mCarBody; ///< The vehicle
    private VehicleConstraint mVehicleConstraint; ///< The vehicle constraint
    private VehicleCollisionTester[] mTesters = new VehicleCollisionTester[3];
    private Mat44 mCameraPivot = Mat44.sIdentity(); ///< The camera pivot, recorded before the physics update to align with the drawn world

    @Override
    protected void initialize() {
        super.initialize();

        float wheel_radius = 0.3f;
        float wheel_width = 0.1f;
        float half_vehicle_length = 2.0f;
        float half_vehicle_width = 0.9f;
        float half_vehicle_height = 0.2f;

        // Create collision testers
        mTesters[0] = new VehicleCollisionTesterRay(Layers.MOVING);
        mTesters[1] = new VehicleCollisionTesterCastSphere(Layers.MOVING, 0.5f * wheel_width);
        mTesters[2] = new VehicleCollisionTesterCastCylinder(Layers.MOVING);

        // Create vehicle body
        Vec3 position = new Vec3(0, 2, 0);
        Shape car_shape = new OffsetCenterOfMassShapeSettings(new Vec3(0, -half_vehicle_height, 0), new BoxShape(new Vec3(half_vehicle_width, half_vehicle_height, half_vehicle_length))).Create().Get();
        BodyCreationSettings car_body_settings = new BodyCreationSettings(car_shape, position, Quat.sRotation(Vec3.sAxisZ(), sInitialRollAngle), EMotionType.EMotionType_Dynamic, Layers.MOVING);
        car_body_settings.set_mOverrideMassProperties(EOverrideMassProperties.EOverrideMassProperties_CalculateInertia);
        car_body_settings.get_mMassPropertiesOverride().set_mMass(1500.0f);
        mCarBody = mBodyInterface.CreateBody(car_body_settings);
        mBodyInterface.AddBody(mCarBody.GetID(), EActivation.EActivation_Activate);

        // Create vehicle constraint
        VehicleConstraintSettings vehicle = new VehicleConstraintSettings();
        vehicle.set_mDrawConstraintSize(0.1f);
        vehicle.set_mMaxPitchRollAngle(sMaxRollAngle);

        // Suspension direction
        Vec3 front_suspension_dir = new Vec3(MathUtils.tan(sFrontSuspensionSidewaysAngle), -1, MathUtils.tan(sFrontSuspensionForwardAngle)).Normalized();
        Vec3 front_steering_axis = new Vec3(-MathUtils.tan(sFrontKingPinAngle), 1, -MathUtils.tan(sFrontCasterAngle)).Normalized();
        Vec3 front_wheel_up = new Vec3(MathUtils.sin(sFrontCamber), MathUtils.cos(sFrontCamber), 0);
        Vec3 front_wheel_forward = new Vec3(-MathUtils.sin(sFrontToe), 0, MathUtils.cos(sFrontToe));
        Vec3 rear_suspension_dir = new Vec3(MathUtils.tan(sRearSuspensionSidewaysAngle), -1, MathUtils.tan(sRearSuspensionForwardAngle)).Normalized();
        Vec3 rear_steering_axis = new Vec3(-MathUtils.tan(sRearKingPinAngle), 1, -MathUtils.tan(sRearCasterAngle)).Normalized();
        Vec3 rear_wheel_up = new Vec3(MathUtils.sin(sRearCamber), MathUtils.cos(sRearCamber), 0);
        Vec3 rear_wheel_forward = new Vec3(-MathUtils.sin(sRearToe), 0, MathUtils.cos(sRearToe));
        Vec3 flip_x = new Vec3(-1, 1, 1);

        // Wheels, left front
        WheelSettingsWV w1 = new WheelSettingsWV();
        w1.set_mPosition(new Vec3(half_vehicle_width, -0.9f * half_vehicle_height, half_vehicle_length - 2.0f * wheel_radius));
        w1.set_mSuspensionDirection(front_suspension_dir);
        w1.set_mSteeringAxis(front_steering_axis);
        w1.set_mWheelUp(front_wheel_up);
        w1.set_mWheelForward(front_wheel_forward);
        w1.set_mSuspensionMinLength(sFrontSuspensionMinLength);
        w1.set_mSuspensionMaxLength(sFrontSuspensionMaxLength);
        w1.get_mSuspensionSpring().set_mFrequency(sFrontSuspensionFrequency);
        w1.get_mSuspensionSpring().set_mDamping(sFrontSuspensionDamping);
        w1.set_mMaxSteerAngle(sMaxSteeringAngle);
        w1.set_mMaxHandBrakeTorque(0.0f); // Front wheel doesn't have hand brake

        // Right front
        WheelSettingsWV w2 = new WheelSettingsWV();
        w2.set_mPosition(new Vec3(-half_vehicle_width, -0.9f * half_vehicle_height, half_vehicle_length - 2.0f * wheel_radius));
        w2.set_mSuspensionDirection(flip_x.Multiply(front_suspension_dir));
        w2.set_mSteeringAxis(flip_x.Multiply(front_steering_axis));
        w2.set_mWheelUp(flip_x.Multiply(front_wheel_up));
        w2.set_mWheelForward(flip_x.Multiply(front_wheel_forward));
        w2.set_mSuspensionMinLength(sFrontSuspensionMinLength);
        w2.set_mSuspensionMaxLength(sFrontSuspensionMaxLength);
        w2.get_mSuspensionSpring().set_mFrequency(sFrontSuspensionFrequency);
        w2.get_mSuspensionSpring().set_mDamping(sFrontSuspensionDamping);
        w2.set_mMaxSteerAngle(sMaxSteeringAngle);
        w2.set_mMaxHandBrakeTorque(0.0f); // Front wheel doesn't have hand brake

        // Left rear
        WheelSettingsWV w3 = new WheelSettingsWV();
        w3.set_mPosition(new Vec3(half_vehicle_width, -0.9f * half_vehicle_height, -half_vehicle_length + 2.0f * wheel_radius));
        w3.set_mSuspensionDirection(rear_suspension_dir);
        w3.set_mSteeringAxis(rear_steering_axis);
        w3.set_mWheelUp(rear_wheel_up);
        w3.set_mWheelForward(rear_wheel_forward);
        w3.set_mSuspensionMinLength(sRearSuspensionMinLength);
        w3.set_mSuspensionMaxLength(sRearSuspensionMaxLength);
        w3.get_mSuspensionSpring().set_mFrequency(sRearSuspensionFrequency);
        w3.get_mSuspensionSpring().set_mDamping(sRearSuspensionDamping);
        w3.set_mMaxSteerAngle(0.0f);

        // Right rear
        WheelSettingsWV w4 = new WheelSettingsWV();
        w4.set_mPosition(new Vec3(-half_vehicle_width, -0.9f * half_vehicle_height, -half_vehicle_length + 2.0f * wheel_radius));
        w4.set_mSuspensionDirection(flip_x.Multiply(rear_suspension_dir));
        w4.set_mSteeringAxis(flip_x.Multiply(rear_steering_axis));
        w4.set_mWheelUp(flip_x.Multiply(rear_wheel_up));
        w4.set_mWheelForward(flip_x.Multiply(rear_wheel_forward));
        w4.set_mSuspensionMinLength(sRearSuspensionMinLength);
        w4.set_mSuspensionMaxLength(sRearSuspensionMaxLength);
        w4.get_mSuspensionSpring().set_mFrequency(sRearSuspensionFrequency);
        w4.get_mSuspensionSpring().set_mDamping(sRearSuspensionDamping);
        w4.set_mMaxSteerAngle(0.0f);

        ArrayWheelSettings wheelSettingsArray = new ArrayWheelSettings();
        wheelSettingsArray.push_back(w1);
        wheelSettingsArray.push_back(w2);
        wheelSettingsArray.push_back(w3);
        wheelSettingsArray.push_back(w4);
        vehicle.set_mWheels(wheelSettingsArray);

        w1.set_mRadius(wheel_radius);
        w1.set_mWidth(wheel_width);
        w2.set_mRadius(wheel_radius);
        w2.set_mWidth(wheel_width);
        w3.set_mRadius(wheel_radius);
        w3.set_mWidth(wheel_width);
        w4.set_mRadius(wheel_radius);
        w4.set_mWidth(wheel_width);

        WheeledVehicleControllerSettings controller = new WheeledVehicleControllerSettings();
        vehicle.set_mController(controller);

        // Differential
        controller.get_mDifferentials().resize(sFourWheelDrive? 2 : 1);
        controller.get_mDifferentials().at(0).set_mLeftWheel(0);
        controller.get_mDifferentials().at(0).set_mRightWheel(1);
        if (sFourWheelDrive)
        {
            controller.get_mDifferentials().at(1).set_mLeftWheel(2);
            controller.get_mDifferentials().at(1).set_mRightWheel(3);

            // Split engine torque
            controller.get_mDifferentials().at(1).set_mEngineTorqueRatio(0.5f);
            controller.get_mDifferentials().at(0).set_mEngineTorqueRatio(0.5f);
        }

        // Anti rollbars
        if (sAntiRollbar)
        {
            ArrayVehicleAntiRollBar mAntiRollBars = vehicle.get_mAntiRollBars();
            mAntiRollBars.resize(2);
            mAntiRollBars.at(0).set_mLeftWheel(0);
            mAntiRollBars.at(0).set_mRightWheel(1);
            mAntiRollBars.at(1).set_mLeftWheel(2);
            mAntiRollBars.at(1).set_mRightWheel(3);
        }

        mVehicleConstraint = new VehicleConstraint(mCarBody, vehicle);

        mPhysicsSystem.AddConstraint(mVehicleConstraint);
//        mPhysicsSystem.AddStepListener(mVehicleConstraint);
    }
}

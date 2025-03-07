#pragma once

#include "Jolt/Jolt.h"
#include "Jolt/RegisterTypes.h"
#include "Jolt/Core/Factory.h"
#include "Jolt/Core/JobSystemThreadPool.h"
#include "Jolt/Math/Vec3.h"
#include "Jolt/Math/Quat.h"
#include "Jolt/Geometry/OrientedBox.h"
#include "Jolt/Physics/PhysicsSystem.h"
#include "Jolt/Physics/StateRecorderImpl.h"
#include "Jolt/Physics/Collision/RayCast.h"
#include "Jolt/Physics/Collision/CastResult.h"
#include "Jolt/Physics/Collision/AABoxCast.h"
#include "Jolt/Physics/Collision/ShapeCast.h"
#include "Jolt/Physics/Collision/CollidePointResult.h"
#include "Jolt/Physics/Collision/Shape/SphereShape.h"
#include "Jolt/Physics/Collision/Shape/BoxShape.h"
#include "Jolt/Physics/Collision/Shape/CapsuleShape.h"
#include "Jolt/Physics/Collision/Shape/TaperedCapsuleShape.h"
#include "Jolt/Physics/Collision/Shape/CylinderShape.h"
#include "Jolt/Physics/Collision/Shape/ConvexHullShape.h"
#include "Jolt/Physics/Collision/Shape/StaticCompoundShape.h"
#include "Jolt/Physics/Collision/Shape/MutableCompoundShape.h"
#include "Jolt/Physics/Collision/Shape/ScaledShape.h"
#include "Jolt/Physics/Collision/Shape/OffsetCenterOfMassShape.h"
#include "Jolt/Physics/Collision/Shape/RotatedTranslatedShape.h"
#include "Jolt/Physics/Collision/Shape/MeshShape.h"
#include "Jolt/Physics/Collision/Shape/HeightFieldShape.h"
#include "Jolt/Physics/Collision/CollisionCollectorImpl.h"
#include "Jolt/Physics/Collision/GroupFilterTable.h"
#include "Jolt/Physics/Collision/CollideShape.h"
#include "Jolt/Physics/Constraints/FixedConstraint.h"
#include "Jolt/Physics/Constraints/PointConstraint.h"
#include "Jolt/Physics/Constraints/DistanceConstraint.h"
#include "Jolt/Physics/Constraints/HingeConstraint.h"
#include "Jolt/Physics/Constraints/ConeConstraint.h"
#include "Jolt/Physics/Constraints/PathConstraint.h"
#include "Jolt/Physics/Constraints/PathConstraintPath.h"
#include "Jolt/Physics/Constraints/PulleyConstraint.h"
#include "Jolt/Physics/Constraints/SliderConstraint.h"
#include "Jolt/Physics/Constraints/SwingTwistConstraint.h"
#include "Jolt/Physics/Constraints/SixDOFConstraint.h"
#include "Jolt/Physics/Constraints/GearConstraint.h"
#include "Jolt/Physics/Constraints/RackAndPinionConstraint.h"
#include "Jolt/Physics/Body/BodyInterface.h"
#include "Jolt/Physics/Body/BodyCreationSettings.h"
#include "Jolt/Physics/Ragdoll/Ragdoll.h"
#include "Jolt/Physics/SoftBody/SoftBodyCreationSettings.h"
#include "Jolt/Physics/SoftBody/SoftBodySharedSettings.h"
#include "Jolt/Physics/SoftBody/SoftBodyShape.h"
#include "Jolt/Physics/SoftBody/SoftBodyMotionProperties.h"
#include "Jolt/Physics/SoftBody/SoftBodyContactListener.h"
#include "Jolt/Physics/SoftBody/SoftBodyManifold.h"
#include "Jolt/Physics/Character/CharacterVirtual.h"
#include "Jolt/Physics/Vehicle/VehicleConstraint.h"
#include "Jolt/Physics/Vehicle/MotorcycleController.h"
#include "Jolt/Physics/Vehicle/TrackedVehicleController.h"
#include "Jolt/Physics/Collision/BroadPhase/BroadPhaseLayerInterfaceTable.h"
#include "Jolt/Physics/Collision/BroadPhase/ObjectVsBroadPhaseLayerFilterTable.h"
#include "Jolt/Physics/Collision/ObjectLayerPairFilterTable.h"
#include "Jolt/Physics/Collision/BroadPhase/BroadPhaseLayerInterfaceMask.h"
#include "Jolt/Physics/Collision/BroadPhase/ObjectVsBroadPhaseLayerFilterMask.h"
#include "Jolt/Physics/Collision/ObjectLayerPairFilterMask.h"
#include "Jolt/Physics/Body/BodyActivationListener.h"
#include "Jolt/Skeleton/SkeletalAnimation.h"
#include "Jolt/Skeleton/SkeletonPose.h"
#include "Jolt/Skeleton/Skeleton.h"

#include <iostream>
//#include <malloc.h>
#include <stdlib.h>
//#include <unistd.h>
//#include <emscripten/em_asm.h>

using namespace std;
//
//#ifdef JPH_DEBUG_RENDERER
//    #include "JoltJS-DebugRenderer.h"
//#endif
//
//// Ensure that we use 32-bit object layers
//static_assert(sizeof(ObjectLayer) == 4);

//// Types that need to be exposed to JavaScript
using uint = JPH::uint;
using uint8 = JPH::uint8;
using uint64 = JPH::uint64;
using ArrayVec3 = JPH::Array<JPH::Vec3>;
using ArrayFloat = JPH::Array<float>;
using ArrayUint = JPH::Array<JPH::uint>;
using ArrayUint8 = JPH::Array<JPH::uint8>;
using Vec3MemRef = JPH::Vec3;
using QuatMemRef = JPH::Quat;
using ArrayQuat = JPH::Array<JPH::Quat>;
using Mat44MemRef = JPH::Mat44;
using ArrayMat44 = JPH::Array<JPH::Mat44>;
using FloatMemRef = float;
using UintMemRef = JPH::uint;
using Uint8MemRef = JPH::uint8;
using SoftBodySharedSettingsVertex = JPH::SoftBodySharedSettings::Vertex;
using SoftBodySharedSettingsFace = JPH::SoftBodySharedSettings::Face;
using SoftBodySharedSettingsEdge = JPH::SoftBodySharedSettings::Edge;
using SoftBodySharedSettingsDihedralBend = JPH::SoftBodySharedSettings::DihedralBend;
using SoftBodySharedSettingsVolume = JPH::SoftBodySharedSettings::Volume;
using SoftBodySharedSettingsInvBind = JPH::SoftBodySharedSettings::InvBind;
using SoftBodySharedSettingsSkinWeight = JPH::SoftBodySharedSettings::SkinWeight;
using SoftBodySharedSettingsSkinned = JPH::SoftBodySharedSettings::Skinned;
using SoftBodySharedSettingsLRA = JPH::SoftBodySharedSettings::LRA;
using SoftBodySharedSettingsVertexAttributes = JPH::SoftBodySharedSettings::VertexAttributes;
using CollideShapeResultFace = JPH::CollideShapeResult::Face;
using ArraySoftBodySharedSettingsVertex = JPH::Array<SoftBodySharedSettingsVertex>;
using ArraySoftBodySharedSettingsFace = JPH::Array<SoftBodySharedSettingsFace>;
using ArraySoftBodySharedSettingsEdge = JPH::Array<SoftBodySharedSettingsEdge>;
using ArraySoftBodySharedSettingsDihedralBend = JPH::Array<SoftBodySharedSettingsDihedralBend>;
using ArraySoftBodySharedSettingsVolume = JPH::Array<SoftBodySharedSettingsVolume>;
using ArraySoftBodySharedSettingsInvBind = JPH::Array<SoftBodySharedSettingsInvBind>;
using ArraySoftBodySharedSettingsSkinWeight = JPH::Array<SoftBodySharedSettingsSkinWeight>;
using ArraySoftBodySharedSettingsSkinned = JPH::Array<SoftBodySharedSettingsSkinned>;
using ArraySoftBodySharedSettingsLRA = JPH::Array<SoftBodySharedSettingsLRA>;
using ArraySoftBodySharedSettingsVertexAttributes = JPH::Array<SoftBodySharedSettingsVertexAttributes>;
using ArraySoftBodyVertex = JPH::Array<JPH::SoftBodyVertex>;
using Vector2 = JPH::Vector<2>;
using ArrayRayCastResult = JPH::Array<JPH::RayCastResult>;
using CastRayAllHitCollisionCollector = JPH::AllHitCollisionCollector<JPH::CastRayCollector>;
using CastRayClosestHitCollisionCollector = JPH::ClosestHitCollisionCollector<JPH::CastRayCollector>;
using CastRayAnyHitCollisionCollector = JPH::AnyHitCollisionCollector<JPH::CastRayCollector>;
using ArrayCollidePointResult = JPH::Array<JPH::CollidePointResult>;
using CollidePointAllHitCollisionCollector = JPH::AllHitCollisionCollector<JPH::CollidePointCollector>;
using CollidePointClosestHitCollisionCollector = JPH::ClosestHitCollisionCollector<JPH::CollidePointCollector>;
using CollidePointAnyHitCollisionCollector = JPH::AnyHitCollisionCollector<JPH::CollidePointCollector>;
using ArrayCollideShapeResult = JPH::Array<JPH::CollideShapeResult>;
using CollideShapeAllHitCollisionCollector = JPH::AllHitCollisionCollector<JPH::CollideShapeCollector>;
using CollideShapeClosestHitCollisionCollector = JPH::ClosestHitCollisionCollector<JPH::CollideShapeCollector>;
using CollideShapeAnyHitCollisionCollector = JPH::AnyHitCollisionCollector<JPH::CollideShapeCollector>;
using ArrayShapeCastResult = JPH::Array<JPH::ShapeCastResult>;
using CastShapeAllHitCollisionCollector = JPH::AllHitCollisionCollector<JPH::CastShapeCollector>;
using CastShapeClosestHitCollisionCollector = JPH::ClosestHitCollisionCollector<JPH::CastShapeCollector>;
using CastShapeAnyHitCollisionCollector = JPH::AnyHitCollisionCollector<JPH::CastShapeCollector>;
using ArrayWheelSettings = JPH::Array<JPH::Ref<JPH::WheelSettings>>;
using ArrayVehicleAntiRollBar = JPH::Array<JPH::VehicleAntiRollBar>;
using ArrayVehicleDifferentialSettings = JPH::Array<JPH::VehicleDifferentialSettings>;
using SkeletalAnimationJointState = JPH::SkeletalAnimation::JointState;
using SkeletalAnimationKeyframe = JPH::SkeletalAnimation::Keyframe;
using SkeletalAnimationAnimatedJoint = JPH::SkeletalAnimation::AnimatedJoint;
using ArraySkeletonKeyframe = JPH::Array<SkeletalAnimationKeyframe>;
using ArraySkeletonAnimatedJoint = JPH::Array<SkeletalAnimationAnimatedJoint>;
using RagdollPart = JPH::RagdollSettings::Part;
using RagdollAdditionalConstraint = JPH::RagdollSettings::AdditionalConstraint;
using ArrayRagdollPart = JPH::Array<RagdollPart>;
using ArrayRagdollAdditionalConstraint = JPH::Array<RagdollAdditionalConstraint>;
using CompoundShapeSubShape = JPH::CompoundShape::SubShape;

// Alias for EBodyType values to avoid clashes
using EBodyType = JPH::EBodyType;
constexpr EBodyType EBodyType_RigidBody = EBodyType::RigidBody;
constexpr EBodyType EBodyType_SoftBody = EBodyType::SoftBody;

// Alias for EMotionType values to avoid clashes
using EMotionType = JPH::EMotionType;
constexpr EMotionType EMotionType_Static = EMotionType::Static;
constexpr EMotionType EMotionType_Kinematic = EMotionType::Kinematic;
constexpr EMotionType EMotionType_Dynamic = EMotionType::Dynamic;

// Alias for EMotionQuality values to avoid clashes
using EMotionQuality = JPH::EMotionQuality;
constexpr EMotionQuality EMotionQuality_Discrete = EMotionQuality::Discrete;
constexpr EMotionQuality EMotionQuality_LinearCast = EMotionQuality::LinearCast;

// Alias for EActivation values to avoid clashes
using EActivation = JPH::EActivation;
constexpr EActivation EActivation_Activate = EActivation::Activate;
constexpr EActivation EActivation_DontActivate = EActivation::DontActivate;

// Alias for EShapeType values to avoid clashes
using EShapeType = JPH::EShapeType;
constexpr EShapeType EShapeType_Convex = EShapeType::Convex;
constexpr EShapeType EShapeType_Compound = EShapeType::Compound;
constexpr EShapeType EShapeType_Decorated = EShapeType::Decorated;
constexpr EShapeType EShapeType_Mesh = EShapeType::Mesh;
constexpr EShapeType EShapeType_HeightField = EShapeType::HeightField;

// Alias for EShapeSubType values to avoid clashes
using EShapeSubType = JPH::EShapeSubType;
constexpr EShapeSubType EShapeSubType_Sphere = EShapeSubType::Sphere;
constexpr EShapeSubType EShapeSubType_Box = EShapeSubType::Box;
constexpr EShapeSubType EShapeSubType_Capsule = EShapeSubType::Capsule;
constexpr EShapeSubType EShapeSubType_TaperedCapsule = EShapeSubType::TaperedCapsule;
constexpr EShapeSubType EShapeSubType_Cylinder = EShapeSubType::Cylinder;
constexpr EShapeSubType EShapeSubType_ConvexHull = EShapeSubType::ConvexHull;
constexpr EShapeSubType EShapeSubType_StaticCompound = EShapeSubType::StaticCompound;
constexpr EShapeSubType EShapeSubType_MutableCompound = EShapeSubType::MutableCompound;
constexpr EShapeSubType EShapeSubType_RotatedTranslated = EShapeSubType::RotatedTranslated;
constexpr EShapeSubType EShapeSubType_Scaled = EShapeSubType::Scaled;
constexpr EShapeSubType EShapeSubType_OffsetCenterOfMass = EShapeSubType::OffsetCenterOfMass;
constexpr EShapeSubType EShapeSubType_Mesh = EShapeSubType::Mesh;
constexpr EShapeSubType EShapeSubType_HeightField = EShapeSubType::HeightField;

// Alias for EConstraintSpace values to avoid clashes
using EConstraintSpace = JPH::EConstraintSpace;
constexpr EConstraintSpace EConstraintSpace_LocalToBodyCOM = EConstraintSpace::LocalToBodyCOM;
constexpr EConstraintSpace EConstraintSpace_WorldSpace = EConstraintSpace::WorldSpace;

// Alias for ESpringMode values to avoid clashes
using ESpringMode = JPH::ESpringMode;
constexpr ESpringMode ESpringMode_FrequencyAndDamping = ESpringMode::FrequencyAndDamping;
constexpr ESpringMode ESpringMode_StiffnessAndDamping = ESpringMode::StiffnessAndDamping;

// Alias for EOverrideMassProperties values to avoid clashes
using EOverrideMassProperties = JPH::EOverrideMassProperties;
constexpr EOverrideMassProperties EOverrideMassProperties_CalculateMassAndInertia = EOverrideMassProperties::CalculateMassAndInertia;
constexpr EOverrideMassProperties EOverrideMassProperties_CalculateInertia = EOverrideMassProperties::CalculateInertia;
constexpr EOverrideMassProperties EOverrideMassProperties_MassAndInertiaProvided = EOverrideMassProperties::MassAndInertiaProvided;

// Alias for EAllowedDOFs values to avoid clashes
using EAllowedDOFs = JPH::EAllowedDOFs;
constexpr EAllowedDOFs EAllowedDOFs_TranslationX = EAllowedDOFs::TranslationX;
constexpr EAllowedDOFs EAllowedDOFs_TranslationY = EAllowedDOFs::TranslationY;
constexpr EAllowedDOFs EAllowedDOFs_TranslationZ = EAllowedDOFs::TranslationZ;
constexpr EAllowedDOFs EAllowedDOFs_RotationX = EAllowedDOFs::RotationX;
constexpr EAllowedDOFs EAllowedDOFs_RotationY = EAllowedDOFs::RotationY;
constexpr EAllowedDOFs EAllowedDOFs_RotationZ = EAllowedDOFs::RotationZ;
constexpr EAllowedDOFs EAllowedDOFs_Plane2D = EAllowedDOFs::Plane2D;
constexpr EAllowedDOFs EAllowedDOFs_All = EAllowedDOFs::All;

// Alias for EStateRecorderState values to avoid clashes
using EStateRecorderState = JPH::EStateRecorderState;
constexpr EStateRecorderState EStateRecorderState_None = EStateRecorderState::None;
constexpr EStateRecorderState EStateRecorderState_Global = EStateRecorderState::Global;
constexpr EStateRecorderState EStateRecorderState_Bodies = EStateRecorderState::Bodies;
constexpr EStateRecorderState EStateRecorderState_Contacts = EStateRecorderState::Contacts;
constexpr EStateRecorderState EStateRecorderState_Constraints = EStateRecorderState::Constraints;
constexpr EStateRecorderState EStateRecorderState_All = EStateRecorderState::All;

// Alias for EBackFaceMode values to avoid clashes
using EBackFaceMode = JPH::EBackFaceMode;
constexpr EBackFaceMode EBackFaceMode_IgnoreBackFaces = EBackFaceMode::IgnoreBackFaces;
constexpr EBackFaceMode EBackFaceMode_CollideWithBackFaces = EBackFaceMode::CollideWithBackFaces;

// Alias for EGroundState values to avoid clashes
using EGroundState = JPH::CharacterBase::EGroundState;
constexpr EGroundState EGroundState_OnGround = EGroundState::OnGround;
constexpr EGroundState EGroundState_OnSteepGround = EGroundState::OnSteepGround;
constexpr EGroundState EGroundState_NotSupported = EGroundState::NotSupported;
constexpr EGroundState EGroundState_InAir = EGroundState::InAir;

// Alias for ValidateResult values to avoid clashes
using ValidateResult = JPH::ValidateResult;
constexpr ValidateResult ValidateResult_AcceptAllContactsForThisBodyPair = ValidateResult::AcceptAllContactsForThisBodyPair;
constexpr ValidateResult ValidateResult_AcceptContact = ValidateResult::AcceptContact;
constexpr ValidateResult ValidateResult_RejectContact = ValidateResult::RejectContact;
constexpr ValidateResult ValidateResult_RejectAllContactsForThisBodyPair = ValidateResult::RejectAllContactsForThisBodyPair;

// Alias for SoftBodyValidateResult values to avoid clashes
using SoftBodyValidateResult = JPH::SoftBodyValidateResult;
constexpr SoftBodyValidateResult SoftBodyValidateResult_AcceptContact = SoftBodyValidateResult::AcceptContact;
constexpr SoftBodyValidateResult SoftBodyValidateResult_RejectContact = SoftBodyValidateResult::RejectContact;

// Alias for EActiveEdgeMode values to avoid clashes
using EActiveEdgeMode = JPH::EActiveEdgeMode;
constexpr EActiveEdgeMode EActiveEdgeMode_CollideOnlyWithActive = EActiveEdgeMode::CollideOnlyWithActive;
constexpr EActiveEdgeMode EActiveEdgeMode_CollideWithAll = EActiveEdgeMode::CollideWithAll;

// Alias for ECollectFacesMode values to avoid clashes
using ECollectFacesMode = JPH::ECollectFacesMode;
constexpr ECollectFacesMode ECollectFacesMode_CollectFaces = ECollectFacesMode::CollectFaces;
constexpr ECollectFacesMode ECollectFacesMode_NoFaces = ECollectFacesMode::NoFaces;

// Alias for EConstraintType values to avoid clashes
using EConstraintType = JPH::EConstraintType;
constexpr EConstraintType EConstraintType_Constraint = EConstraintType::Constraint;
constexpr EConstraintType EConstraintType_TwoBodyConstraint = EConstraintType::TwoBodyConstraint;

// Alias for EConstraintSubType values to avoid clashes
using EConstraintSubType = JPH::EConstraintSubType;
constexpr EConstraintSubType EConstraintSubType_Fixed = EConstraintSubType::Fixed;
constexpr EConstraintSubType EConstraintSubType_Point = EConstraintSubType::Point;
constexpr EConstraintSubType EConstraintSubType_Hinge = EConstraintSubType::Hinge;
constexpr EConstraintSubType EConstraintSubType_Slider = EConstraintSubType::Slider;
constexpr EConstraintSubType EConstraintSubType_Distance = EConstraintSubType::Distance;
constexpr EConstraintSubType EConstraintSubType_Cone = EConstraintSubType::Cone;
constexpr EConstraintSubType EConstraintSubType_SwingTwist = EConstraintSubType::SwingTwist;
constexpr EConstraintSubType EConstraintSubType_SixDOF = EConstraintSubType::SixDOF;
constexpr EConstraintSubType EConstraintSubType_Path = EConstraintSubType::Path;
constexpr EConstraintSubType EConstraintSubType_Vehicle = EConstraintSubType::Vehicle;
constexpr EConstraintSubType EConstraintSubType_RackAndPinion = EConstraintSubType::RackAndPinion;
constexpr EConstraintSubType EConstraintSubType_Gear = EConstraintSubType::Gear;
constexpr EConstraintSubType EConstraintSubType_Pulley = EConstraintSubType::Pulley;

/// Alias for EPathRotationConstraintType to avoid clash
using EPathRotationConstraintType = JPH::EPathRotationConstraintType;
constexpr EPathRotationConstraintType EPathRotationConstraintType_Free = EPathRotationConstraintType::Free;
constexpr EPathRotationConstraintType EPathRotationConstraintType_ConstrainAroundTangent = EPathRotationConstraintType::ConstrainAroundTangent;
constexpr EPathRotationConstraintType EPathRotationConstraintType_ConstrainAroundNormal = EPathRotationConstraintType::ConstrainAroundNormal;
constexpr EPathRotationConstraintType EPathRotationConstraintType_ConstrainAroundBinormal = EPathRotationConstraintType::ConstrainAroundBinormal;
constexpr EPathRotationConstraintType EPathRotationConstraintType_ConstrainToPath = EPathRotationConstraintType::ConstrainToPath;
constexpr EPathRotationConstraintType EPathRotationConstraintType_FullyConstrained = EPathRotationConstraintType::FullyConstrained;

// Alias for SixDOFConstraintSettings::EAxis to avoid clashes
using SixDOFConstraintSettings_EAxis = JPH::SixDOFConstraintSettings::EAxis;
constexpr SixDOFConstraintSettings_EAxis SixDOFConstraintSettings_EAxis_TranslationX = SixDOFConstraintSettings_EAxis::TranslationX;
constexpr SixDOFConstraintSettings_EAxis SixDOFConstraintSettings_EAxis_TranslationY = SixDOFConstraintSettings_EAxis::TranslationY;
constexpr SixDOFConstraintSettings_EAxis SixDOFConstraintSettings_EAxis_TranslationZ = SixDOFConstraintSettings_EAxis::TranslationZ;
constexpr SixDOFConstraintSettings_EAxis SixDOFConstraintSettings_EAxis_RotationX = SixDOFConstraintSettings_EAxis::RotationX;
constexpr SixDOFConstraintSettings_EAxis SixDOFConstraintSettings_EAxis_RotationY = SixDOFConstraintSettings_EAxis::RotationY;
constexpr SixDOFConstraintSettings_EAxis SixDOFConstraintSettings_EAxis_RotationZ = SixDOFConstraintSettings_EAxis::RotationZ;

// Alias for EMotorState values to avoid clashes
using EMotorState = JPH::EMotorState;
constexpr EMotorState EMotorState_Off = EMotorState::Off;
constexpr EMotorState EMotorState_Velocity = EMotorState::Velocity;
constexpr EMotorState EMotorState_Position = EMotorState::Position;

// Alias for ETransmissionMode values to avoid clashes
using ETransmissionMode = JPH::ETransmissionMode;
constexpr ETransmissionMode ETransmissionMode_Auto = ETransmissionMode::Auto;
constexpr ETransmissionMode ETransmissionMode_Manual = ETransmissionMode::Manual;

// Defining ETireFrictionDirection since we cannot pass references to float
enum ETireFrictionDirection
{
    ETireFrictionDirection_Longitudinal,
    ETireFrictionDirection_Lateral
};

// Alias for ESwingType values to avoid clashes
using ESwingType = JPH::ESwingType;
constexpr ESwingType ESwingType_Cone = ESwingType::Cone;
constexpr ESwingType ESwingType_Pyramid = ESwingType::Pyramid;

// Alias for EBendType values to avoid clashes
using SoftBodySharedSettings_EBendType = JPH::SoftBodySharedSettings::EBendType;
constexpr SoftBodySharedSettings_EBendType SoftBodySharedSettings_EBendType_None = SoftBodySharedSettings_EBendType::None;
constexpr SoftBodySharedSettings_EBendType SoftBodySharedSettings_EBendType_Distance = SoftBodySharedSettings_EBendType::Distance;
constexpr SoftBodySharedSettings_EBendType SoftBodySharedSettings_EBendType_Dihedral = SoftBodySharedSettings_EBendType::Dihedral;

// Alias for ELRAType values to avoid clashes
using SoftBodySharedSettings_ELRAType = JPH::SoftBodySharedSettings::ELRAType;
constexpr SoftBodySharedSettings_ELRAType SoftBodySharedSettings_ELRAType_None = SoftBodySharedSettings_ELRAType::None;
constexpr SoftBodySharedSettings_ELRAType SoftBodySharedSettings_ELRAType_EuclideanDistance = SoftBodySharedSettings_ELRAType::EuclideanDistance;
constexpr SoftBodySharedSettings_ELRAType SoftBodySharedSettings_ELRAType_GeodesicDistance = SoftBodySharedSettings_ELRAType::GeodesicDistance;

// Custom class to be able to create new instance.
class Jolt
{
public:
    static JPH::BodyCreationSettings* New_BodyCreationSettings() {
        return new JPH::BodyCreationSettings();
    }
    static JPH::BodyCreationSettings* New_BodyCreationSettings(const JPH::ShapeSettings* inShape, JPH::RVec3Arg inPosition, JPH::QuatArg inRotation, JPH::EMotionType inMotionType, JPH::ObjectLayer inObjectLayer) {
        return new JPH::BodyCreationSettings(inShape, inPosition, inRotation, inMotionType, inObjectLayer);
    }
    static JPH::BodyCreationSettings* New_BodyCreationSettings(const JPH::Shape* inShape, JPH::RVec3Arg inPosition, JPH::QuatArg inRotation, JPH::EMotionType inMotionType, JPH::ObjectLayer inObjectLayer) {
        return new JPH::BodyCreationSettings(inShape, inPosition, inRotation, inMotionType, inObjectLayer);
    }

    static JPH::Mat44* New_Mat44() {
        return new JPH::Mat44();
    }
    static JPH::Mat44* New_Mat44(JPH::Vec4& inC1, JPH::Vec4& inC2, JPH::Vec4& inC3, JPH::Vec4& inC4) {
        return new JPH::Mat44(inC1, inC2, inC3, inC4);
    }
    static JPH::Mat44* New_Mat44(JPH::Vec4& inC1, JPH::Vec4& inC2, JPH::Vec4& inC3, JPH::Vec3& inC4) {
        return new JPH::Mat44(inC1, inC2, inC3, inC4);
    }

    static JPH::Vec3* New_Vec3() {
        return new JPH::Vec3();
    }
    static JPH::Vec3* New_Vec3(float inX, float inY, float inZ) {
        return new JPH::Vec3(inX, inY, inZ);
    }
    static JPH::Vec3* New_Vec3(const JPH::Vec3 &inRHS) {
        return new JPH::Vec3(inRHS);
    }
    static JPH::Vec3* New_Vec3(const JPH::Float3 &inV) {
        return new JPH::Vec3(inV);
    }

    static JPH::Vec4* New_Vec4() {
        return new JPH::Vec4();
    }
    static JPH::Vec4* New_Vec4(float inX, float inY, float inZ, float inW) {
        return new JPH::Vec4(inX, inY, inZ, inW);
    }
    static JPH::Vec4* New_Vec4(const JPH::Vec4 &inRHS) {
        return new JPH::Vec4(inRHS);
    }
    static JPH::Vec4* New_Vec4(const JPH::Vec3 &inV, float inW) {
        return new JPH::Vec4(inV, inW);
    }
};

// Callback for traces
static void TraceImpl(const char *inFMT, ...)
{
    // Format the message
    va_list list;
    va_start(list, inFMT);
    char buffer[1024];
    vsnprintf(buffer, sizeof(buffer), inFMT, list);

    // Print to the TTY
    cout << buffer << endl;
}

#ifdef JPH_ENABLE_ASSERTS

// Callback for asserts
static bool AssertFailedImpl(const char *inExpression, const char *inMessage, const char *inFile, JPH::uint inLine)
{
    // Print to the TTY
    cout << inFile << ":" << inLine << ": (" << inExpression << ") " << (inMessage != nullptr? inMessage : "") << endl;

    // Breakpoint
    return true;
};

#endif // JPH_ENABLE_ASSERTS

/// Main API for JavaScript
class JoltInterface
{
public:
    /// Constructor
    JoltInterface(
        JPH::BroadPhaseLayerInterface *mBroadPhaseLayerInterface,
        JPH::ObjectVsBroadPhaseLayerFilter *mObjectVsBroadPhaseLayerFilter,
        JPH::ObjectLayerPairFilter * mObjectLayerPairFilter,
        JPH::uint mMaxBodies = 10240,
        JPH::uint mMaxBodyPairs = 65536,
        JPH::uint mMaxContactConstraints = 10240,
        JPH::uint mTempAllocatorSize = 10 * 1024 * 1024
    )
    {
        // Install callbacks
        JPH::Trace = TraceImpl;
        JPH_IF_ENABLE_ASSERTS(JPH::AssertFailed = AssertFailedImpl;)

        // Create a factory
        JPH::Factory::sInstance = new JPH::Factory();

        // Register all Jolt physics types
        JPH::RegisterTypes();

        // Init temp allocator
        mTempAllocator = new JPH::TempAllocatorImpl(mTempAllocatorSize);

        // Init the physics system
        constexpr JPH::uint cNumBodyMutexes = 0;
        mPhysicsSystem = new JPH::PhysicsSystem();
        mPhysicsSystem->Init(mMaxBodies, cNumBodyMutexes, mMaxBodyPairs, mMaxContactConstraints, *mBroadPhaseLayerInterface, *mObjectVsBroadPhaseLayerFilter, *mObjectLayerPairFilter);
    }

    /// Destructor
    ~JoltInterface()
    {
        // Destroy subsystems
        delete mPhysicsSystem;
        delete mBroadPhaseLayerInterface;
        delete mObjectVsBroadPhaseLayerFilter;
        delete mObjectLayerPairFilter;
        delete mTempAllocator;
        delete JPH::Factory::sInstance;
        JPH::Factory::sInstance = nullptr;
        JPH::UnregisterTypes();
    }

    void ClearWorld() {
        JPH::PhysicsSystem& physicsSystem = *mPhysicsSystem;

        // Step 1: Remove and delete all constraints
        JPH::Array<JPH::Ref<JPH::Constraint>> constraints = physicsSystem.GetConstraints();
        for (JPH::Ref<JPH::Constraint>& constraintRef : constraints) {
            if (constraintRef) { // Check if the reference is valid
                JPH::Constraint* constraint = constraintRef.GetPtr();
                physicsSystem.RemoveConstraint(constraint);
            }
        }

        // Step 2: Remove and destroy all bodies
        JPH::BodyInterface& bodyInterface = physicsSystem.GetBodyInterface();
        JPH::BodyIDVector bodyIDs;
        physicsSystem.GetBodies(bodyIDs); // Fills the vector with all body IDs

        if (!bodyIDs.empty()) {
            // Remove bodies from the simulation
            bodyInterface.RemoveBodies(bodyIDs.data(), bodyIDs.size());
            // Destroy bodies to free their memory
            bodyInterface.DestroyBodies(bodyIDs.data(), bodyIDs.size());
        }
    }

    /// Step the world
    void Step(float inDeltaTime, int inCollisionSteps)
    {
        mPhysicsSystem->Update(inDeltaTime, inCollisionSteps, mTempAllocator, &mJobSystem);
    }

    /// Access to the physics system
    JPH::PhysicsSystem * GetPhysicsSystem()
    {
        return mPhysicsSystem;
    }

    /// Access to the temp allocator
    JPH::TempAllocator * GetTempAllocator()
    {
        return mTempAllocator;
    }

    /// Access the default object layer pair filter
    JPH::ObjectLayerPairFilter *GetObjectLayerPairFilter()
    {
        return mObjectLayerPairFilter;
    }

    /// Access the default object vs broadphase layer filter
    JPH::ObjectVsBroadPhaseLayerFilter *GetObjectVsBroadPhaseLayerFilter()
    {
        return mObjectVsBroadPhaseLayerFilter;
    }

//    /// Get the total reserved memory in bytes
//    /// See: https://github.com/emscripten-core/emscripten/blob/7459cab167138419168b5ac5eacf74702d5a3dae/test/core/test_mallinfo.c#L16-L18
//    static size_t sGetTotalMemory()
//    {
//        return (size_t)EM_ASM_PTR(return HEAP8.length);
//    }
//
//    /// Get the amount of free memory in bytes
//    /// See: https://github.com/emscripten-core/emscripten/blob/7459cab167138419168b5ac5eacf74702d5a3dae/test/core/test_mallinfo.c#L20-L25
//    static size_t sGetFreeMemory()
//    {
//        struct mallinfo i = mallinfo();
//        uintptr_t total_memory = sGetTotalMemory();
//        uintptr_t dynamic_top = (uintptr_t)sbrk(0);
//        return total_memory - dynamic_top + i.fordblks;
//    }

private:
    JPH::TempAllocatorImpl * mTempAllocator;
    JPH::JobSystemThreadPool mJobSystem { JPH::cMaxPhysicsJobs, JPH::cMaxPhysicsBarriers, min<int>(thread::hardware_concurrency() - 1, 16) }; // Limit to 16 threads since we limit the webworker thread pool size to this as well
    JPH::BroadPhaseLayerInterface *mBroadPhaseLayerInterface = nullptr;
    JPH::ObjectVsBroadPhaseLayerFilter *mObjectVsBroadPhaseLayerFilter = nullptr;
    JPH::ObjectLayerPairFilter * mObjectLayerPairFilter = nullptr;
    JPH::PhysicsSystem * mPhysicsSystem = nullptr;
};

// Helper class to store information about the memory layout of SoftBodyVertex
class SoftBodyVertexTraits
{
public:
    static constexpr JPH::uint mPreviousPositionOffset = offsetof(JPH::SoftBodyVertex, mPreviousPosition);
    static constexpr JPH::uint mPositionOffset = offsetof(JPH::SoftBodyVertex, mPosition);
    static constexpr JPH::uint mVelocityOffset = offsetof(JPH::SoftBodyVertex, mVelocity);
};

/// Helper class to extract triangles from the shape
class ShapeGetTriangles
{
public:
    ShapeGetTriangles(const JPH::Shape *inShape, const JPH::AABox &inBox, JPH::Vec3Arg inPositionCOM, JPH::QuatArg inRotation, JPH::Vec3Arg inScale)
    {
        const size_t cBlockSize = 8096;

        // First collect all leaf shapes
        JPH::AllHitCollisionCollector<JPH::TransformedShapeCollector> collector;
        inShape->CollectTransformedShapes(inBox, inPositionCOM, inRotation, inScale, JPH::SubShapeIDCreator(), collector, { });

        size_t cur_pos = 0;

        // Iterate the leaf shapes
        for (const JPH::TransformedShape &ts : collector.mHits)
        {
            // Start iterating triangles
            JPH::Shape::GetTrianglesContext context;
            ts.GetTrianglesStart(context, inBox, JPH::RVec3::sZero());

            for (;;)
            {
                // Ensure we have space to get more triangles
                size_t tri_left = mMaterials.size() - cur_pos;
                if (tri_left < JPH::Shape::cGetTrianglesMinTrianglesRequested)
                {
                    mVertices.resize(mVertices.size() + 3 * cBlockSize);
                    mMaterials.resize(mMaterials.size() + cBlockSize);
                    tri_left = mMaterials.size() - cur_pos;
                }

                // Fetch next batch
                int count = ts.GetTrianglesNext(context, tri_left, mVertices.data() + 3 * cur_pos, mMaterials.data() + cur_pos);
                if (count == 0)
                {
                    // We're done
                    mVertices.resize(3 * cur_pos);
                    mMaterials.resize(cur_pos);
                    break;
                }

                cur_pos += count;
            }
        }

        // Free excess memory
        mVertices.shrink_to_fit();
        mMaterials.shrink_to_fit();
    }

    int GetNumTriangles() const
    {
        return (int)mMaterials.size();
    }

    int GetVerticesSize() const
    {
        return (int)mVertices.size() * sizeof(JPH::Float3);
    }

    const JPH::Float3 * GetVerticesData() const
    {
        return mVertices.data();
    }

    const JPH::PhysicsMaterial * GetMaterial(int inTriangle) const
    {
        return mMaterials[inTriangle];
    }

private:
    JPH::Array<JPH::Float3> mVertices;
    JPH::Array<const JPH::PhysicsMaterial *>	mMaterials;
};

/// A wrapper around ContactListener that is compatible with JavaScript
class ContactListenerEm: public JPH::ContactListener
{
public:
    // JavaScript compatible virtual functions
    virtual int OnContactValidate(const JPH::Body &inBody1, const JPH::Body &inBody2, const JPH::RVec3 *inBaseOffset, const JPH::CollideShapeResult &inCollisionResult) = 0;

    // Functions that call the JavaScript compatible virtual functions
    virtual JPH::ValidateResult OnContactValidate(const JPH::Body &inBody1, const JPH::Body &inBody2, JPH::RVec3Arg inBaseOffset, const JPH::CollideShapeResult &inCollisionResult) override
    {
        return (JPH::ValidateResult)OnContactValidate(inBody1, inBody2, &inBaseOffset, inCollisionResult);
    }
};

/// A wrapper around SoftBodyContactListener that is compatible with JavaScript
class SoftBodyContactListenerEm: public JPH::SoftBodyContactListener
{
public:
    // JavaScript compatible virtual functions
    virtual int OnSoftBodyContactValidate(const JPH::Body &inSoftBody, const JPH::Body &inOtherBody, JPH::SoftBodyContactSettings *ioSettings) = 0;

    // Functions that call the JavaScript compatible virtual functions
    virtual JPH::SoftBodyValidateResult	OnSoftBodyContactValidate(const JPH::Body &inSoftBody, const JPH::Body &inOtherBody, JPH::SoftBodyContactSettings &ioSettings)
    {
        return (JPH::SoftBodyValidateResult)OnSoftBodyContactValidate(inSoftBody, inOtherBody, &ioSettings);
    }
};

/// A wrapper around CharacterContactListener that is compatible with JavaScript
class CharacterContactListenerEm: public JPH::CharacterContactListener
{
public:
    // JavaScript compatible virtual functions
    virtual void OnContactAdded(const JPH::CharacterVirtual *inCharacter, const JPH::BodyID &inBodyID2, const JPH::SubShapeID &inSubShapeID2, const JPH::RVec3 *inContactPosition, const JPH::Vec3 *inContactNormal, JPH::CharacterContactSettings &ioSettings) = 0;
    virtual void OnCharacterContactAdded(const JPH::CharacterVirtual *inCharacter, const JPH::CharacterVirtual *inOtherCharacter, const JPH::SubShapeID &inSubShapeID2, const JPH::RVec3 *inContactPosition, const JPH::Vec3 *inContactNormal, JPH::CharacterContactSettings &ioSettings) = 0;
    virtual void OnContactSolve(const JPH::CharacterVirtual *inCharacter, const JPH::BodyID &inBodyID2, const JPH::SubShapeID &inSubShapeID2, const JPH::RVec3 *inContactPosition, const JPH::Vec3 *inContactNormal, const JPH::Vec3 *inContactVelocity, const JPH::PhysicsMaterial *inContactMaterial, const JPH::Vec3 *inCharacterVelocity, JPH::Vec3 &ioNewCharacterVelocity) = 0;
    virtual void OnCharacterContactSolve(const JPH::CharacterVirtual *inCharacter, const JPH::CharacterVirtual *inOtherCharacter, const JPH::SubShapeID &inSubShapeID2, const JPH::RVec3 *inContactPosition, const JPH::Vec3 *inContactNormal, const JPH::Vec3 *inContactVelocity, const JPH::PhysicsMaterial *inContactMaterial, const JPH::Vec3 *inCharacterVelocity, JPH::Vec3 &ioNewCharacterVelocity) = 0;

    // Functions that call the JavaScript compatible virtual functions
    virtual void OnContactAdded(const JPH::CharacterVirtual *inCharacter, const JPH::BodyID &inBodyID2, const JPH::SubShapeID &inSubShapeID2, JPH::RVec3Arg inContactPosition, JPH::Vec3Arg inContactNormal, JPH::CharacterContactSettings &ioSettings) override
    {
        OnContactAdded(inCharacter, inBodyID2, inSubShapeID2, &inContactPosition, &inContactNormal, ioSettings);
    }

    virtual void OnCharacterContactAdded(const JPH::CharacterVirtual *inCharacter, const JPH::CharacterVirtual *inOtherCharacter, const JPH::SubShapeID &inSubShapeID2, JPH::RVec3Arg inContactPosition, JPH::Vec3Arg inContactNormal, JPH::CharacterContactSettings &ioSettings) override
    {
        OnCharacterContactAdded(inCharacter, inOtherCharacter, inSubShapeID2, &inContactPosition, &inContactNormal, ioSettings);
    }

    virtual void OnContactSolve(const JPH::CharacterVirtual *inCharacter, const JPH::BodyID &inBodyID2, const JPH::SubShapeID &inSubShapeID2, JPH::RVec3Arg inContactPosition, JPH::Vec3Arg inContactNormal, JPH::Vec3Arg inContactVelocity, const JPH::PhysicsMaterial *inContactMaterial, JPH::Vec3Arg inCharacterVelocity, JPH::Vec3 &ioNewCharacterVelocity) override
    {
        OnContactSolve(inCharacter, inBodyID2, inSubShapeID2, &inContactPosition, &inContactNormal, &inContactVelocity, inContactMaterial, &inCharacterVelocity, ioNewCharacterVelocity);
    }

    virtual void OnCharacterContactSolve(const JPH::CharacterVirtual *inCharacter, const JPH::CharacterVirtual *inOtherCharacter, const JPH::SubShapeID &inSubShapeID2, JPH::RVec3Arg inContactPosition, JPH::Vec3Arg inContactNormal, JPH::Vec3Arg inContactVelocity, const JPH::PhysicsMaterial *inContactMaterial, JPH::Vec3Arg inCharacterVelocity, JPH::Vec3 &ioNewCharacterVelocity) override
    {
        OnCharacterContactSolve(inCharacter, inOtherCharacter, inSubShapeID2, &inContactPosition, &inContactNormal, &inContactVelocity, inContactMaterial, &inCharacterVelocity, ioNewCharacterVelocity);
    }
};

/// A wrapper around the physics step listener that is compatible with JavaScript (JS doesn't like multiple inheritance)
class VehicleConstraintStepListener : public JPH::PhysicsStepListener
{
public:
    VehicleConstraintStepListener(JPH::VehicleConstraint *inVehicleConstraint)
    {
        mInstance = inVehicleConstraint;
    }

    virtual void OnStep(const JPH::PhysicsStepListenerContext &inContext) override
    {
        JPH::PhysicsStepListener *instance = mInstance;
        instance->OnStep(inContext);
    }

private:
    JPH::VehicleConstraint * mInstance;
};

/// Wrapper class around ObjectVsBroadPhaseLayerFilter to make it compatible with JavaScript (JS cannot pass parameter by value)
class ObjectVsBroadPhaseLayerFilterEm : public JPH::ObjectVsBroadPhaseLayerFilter
{
public:
    virtual bool ShouldCollide(JPH::ObjectLayer inLayer1, JPH::BroadPhaseLayer *inLayer2) const = 0;

    virtual bool ShouldCollide(JPH::ObjectLayer inLayer1, JPH::BroadPhaseLayer inLayer2) const
    {
        return ShouldCollide(inLayer1, &inLayer2);
    }
};

/// Wrapper class around BroadPhaseLayerInterface to make it compatible with JavaScript (JS cannot return parameter by value)
class BroadPhaseLayerInterfaceEm : public JPH::BroadPhaseLayerInterface
{
public:
    virtual unsigned short GetBPLayer(JPH::ObjectLayer inLayer) const = 0;

    virtual JPH::BroadPhaseLayer GetBroadPhaseLayer(JPH::ObjectLayer inLayer) const override
    {
        return JPH::BroadPhaseLayer(GetBPLayer(inLayer));
    }

#if defined(JPH_EXTERNAL_PROFILE) || defined(JPH_PROFILE_ENABLED)
    /// Get the user readable name of a broadphase layer (debugging purposes)
    virtual const char * GetBroadPhaseLayerName(JPH::BroadPhaseLayer inLayer) const override
    {
        return "Undefined";
    }
#endif // JPH_EXTERNAL_PROFILE || JPH_PROFILE_ENABLED
};

/// A wrapper around the vehicle constraint callbacks that is compatible with JavaScript
class VehicleConstraintCallbacksEm
{
public:
    virtual ~VehicleConstraintCallbacksEm() = default;

    void SetVehicleConstraint(JPH::VehicleConstraint &inConstraint)
    {
        inConstraint.SetCombineFriction([this](JPH::uint inWheelIndex, float &ioLongitudinalFriction, float &ioLateralFriction, const JPH::Body &inBody2, const JPH::SubShapeID &inSubShapeID2) {
            ioLongitudinalFriction = GetCombinedFriction(inWheelIndex, ETireFrictionDirection_Longitudinal, ioLongitudinalFriction, inBody2, inSubShapeID2);
            ioLateralFriction = GetCombinedFriction(inWheelIndex, ETireFrictionDirection_Lateral, ioLateralFriction, inBody2, inSubShapeID2);
        });
        inConstraint.SetPreStepCallback([this](JPH::VehicleConstraint &inVehicle, const JPH::PhysicsStepListenerContext &inContext) {
            OnPreStepCallback(inVehicle, inContext);
        });
        inConstraint.SetPostCollideCallback([this](JPH::VehicleConstraint &inVehicle, const JPH::PhysicsStepListenerContext &inContext) {
            OnPostCollideCallback(inVehicle, inContext);
        });
        inConstraint.SetPostStepCallback([this](JPH::VehicleConstraint &inVehicle, const JPH::PhysicsStepListenerContext &inContext) {
            OnPostStepCallback(inVehicle, inContext);
        });
    }

    virtual float GetCombinedFriction(unsigned int inWheelIndex, ETireFrictionDirection inTireFrictionDirection, float inTireFriction, const JPH::Body &inBody2, const JPH::SubShapeID &inSubShapeID2) = 0;
    virtual void OnPreStepCallback(JPH::VehicleConstraint &inVehicle, const JPH::PhysicsStepListenerContext &inContext) = 0;
    virtual void OnPostCollideCallback(JPH::VehicleConstraint &inVehicle, const JPH::PhysicsStepListenerContext &inContext) = 0;
    virtual void OnPostStepCallback(JPH::VehicleConstraint &inVehicle, const JPH::PhysicsStepListenerContext &inContext) = 0;
};

/// The tire max impulse callback returns multiple parameters, so we need to store them in a class
class TireMaxImpulseCallbackResult
{
public:
    float mLongitudinalImpulse;
    float mLateralImpulse;
};

/// A wrapper around the wheeled vehicle controller callbacks that is compatible with JavaScript
class WheeledVehicleControllerCallbacksEm
{
public:
    virtual ~WheeledVehicleControllerCallbacksEm() = default;

    void SetWheeledVehicleController(JPH::WheeledVehicleController &inController)
    {
        inController.SetTireMaxImpulseCallback([this](JPH::uint inWheelIndex, float &outLongitudinalImpulse, float &outLateralImpulse, float inSuspensionImpulse, float inLongitudinalFriction, float inLateralFriction, float inLongitudinalSlip, float inLateralSlip, float inDeltaTime) {
            // Pre-fill the structure with default calculated values
            TireMaxImpulseCallbackResult result;
            result.mLongitudinalImpulse = inLongitudinalFriction * inSuspensionImpulse;
            result.mLateralImpulse = inLateralFriction * inSuspensionImpulse;

            OnTireMaxImpulseCallback(inWheelIndex, &result, inSuspensionImpulse, inLongitudinalFriction, inLateralFriction, inLongitudinalSlip, inLateralSlip, inDeltaTime);

            // Read the results
            outLongitudinalImpulse = result.mLongitudinalImpulse;
            outLateralImpulse = result.mLateralImpulse;
        });
    }

    virtual void OnTireMaxImpulseCallback(JPH::uint inWheelIndex, TireMaxImpulseCallbackResult *outResult, float inSuspensionImpulse, float inLongitudinalFriction, float inLateralFriction, float inLongitudinalSlip, float inLateralSlip, float inDeltaTime) = 0;
};

class PathConstraintPathEm: public JPH::PathConstraintPath
{
public:
    virtual float  GetClosestPoint(JPH::Vec3Arg inPosition, float inFractionHint) const
    {
        return GetClosestPoint(&inPosition, inFractionHint);
    }

    virtual void GetPointOnPath(float inFraction, JPH::Vec3 &outPathPosition, JPH::Vec3 &outPathTangent, JPH::Vec3 &outPathNormal, JPH::Vec3 &outPathBinormal) const
    {
        GetPointOnPath(inFraction, &outPathPosition, &outPathTangent, &outPathNormal, &outPathBinormal);
    }

    virtual float GetClosestPoint(const JPH::Vec3 *inPosition, float inFractionHint) const = 0;
    virtual void GetPointOnPath(float inFraction, JPH::Vec3 *outPathPosition, JPH::Vec3 *outPathTangent, JPH::Vec3 *outPathNormal, JPH::Vec3 *outPathBinormal) const = 0;
};

class HeightFieldShapeConstantValues
{
public:
    /// Value used to create gaps in the height field
    static constexpr float cNoCollisionValue = JPH::HeightFieldShapeConstants::cNoCollisionValue;
};

// DEBUG RENDERER

#include "Jolt/Renderer/DebugRendererSimple.h"

using BodyManagerDrawSettings = JPH::BodyManager::DrawSettings;
using DebugRendererVertex = JPH::DebugRenderer::Vertex;
using DebugRendererTriangle = JPH::DebugRenderer::Triangle;
using DebugArrayTriangle = JPH::Array<DebugRendererTriangle>;

using ECullMode = JPH::DebugRenderer::ECullMode;
constexpr ECullMode ECullMode_CullBackFace = ECullMode::CullBackFace;
constexpr ECullMode ECullMode_CullFrontFace = ECullMode::CullFrontFace;
constexpr ECullMode ECullMode_Off = ECullMode::Off;

using ECastShadow = JPH::DebugRenderer::ECastShadow;
constexpr ECastShadow ECastShadow_On = ECastShadow::On;
constexpr ECastShadow ECastShadow_Off = ECastShadow::Off;

using EDrawMode = JPH::DebugRenderer::EDrawMode;
constexpr EDrawMode EDrawMode_Solid = EDrawMode::Solid;
constexpr EDrawMode EDrawMode_Wireframe = EDrawMode::Wireframe;

using EShapeColor = JPH::BodyManager::EShapeColor;
constexpr EShapeColor EShapeColor_InstanceColor = EShapeColor::InstanceColor;
constexpr EShapeColor EShapeColor_ShapeTypeColor = EShapeColor::ShapeTypeColor;
constexpr EShapeColor EShapeColor_MotionTypeColor = EShapeColor::MotionTypeColor;
constexpr EShapeColor EShapeColor_SleepColor = EShapeColor::SleepColor;
constexpr EShapeColor EShapeColor_IslandColor = EShapeColor::IslandColor;
constexpr EShapeColor EShapeColor_MaterialColor = EShapeColor::MaterialColor;

using ESoftBodyConstraintColor = JPH::ESoftBodyConstraintColor;
constexpr ESoftBodyConstraintColor ESoftBodyConstraintColor_ConstraintType = ESoftBodyConstraintColor::ConstraintType;
constexpr ESoftBodyConstraintColor ESoftBodyConstraintColor_ConstraintGroup = ESoftBodyConstraintColor::ConstraintGroup;
constexpr ESoftBodyConstraintColor ESoftBodyConstraintColor_ConstraintOrder = ESoftBodyConstraintColor::ConstraintOrder;

class ShapeFilterEm : public JPH::ShapeFilter
{
    public:

        virtual bool ShouldCollide_1(const JPH::Shape *inShape2, const JPH::SubShapeID &inSubShapeIDOfShape2) const = 0;
        virtual bool ShouldCollide_2(const JPH::Shape *inShape1, const JPH::SubShapeID &inSubShapeIDOfShape1, const JPH::Shape *inShape2, const JPH::SubShapeID &inSubShapeIDOfShape2) const = 0;

        virtual bool ShouldCollide(const JPH::Shape *inShape2, const JPH::SubShapeID &inSubShapeIDOfShape2) const
        {
            return ShouldCollide_1(inShape2, inSubShapeIDOfShape2);
        }

        virtual bool ShouldCollide(const JPH::Shape *inShape1, const JPH::SubShapeID &inSubShapeIDOfShape1, const JPH::Shape *inShape2, const JPH::SubShapeID &inSubShapeIDOfShape2) const
        {
            return ShouldCollide_2(inShape1, inSubShapeIDOfShape1, inShape2, inSubShapeIDOfShape2);
        }
};

class DebugRendererEm : public JPH::DebugRenderer
{
    public:

        DebugRendererEm()
        {
            Initialize();
        }

        void DrawBodies(JPH::PhysicsSystem *inSystem, JPH::BodyManager::DrawSettings *inDrawSettings)
        {
           inSystem->DrawBodies(*inDrawSettings, this);
        }
        void DrawBodies(JPH::PhysicsSystem *inSystem)
        {
           inSystem->DrawBodies(JPH::BodyManager::DrawSettings(), this);
        }

        virtual void DrawMesh(const JPH::RMat44 &inModelMatrix, const DebugArrayTriangle &triangleArray, const JPH::Color &inModelColor, ECullMode inCullMode, EDrawMode inDrawMode) = 0;

        virtual void DrawGeometry(JPH::RMat44Arg inModelMatrix, const JPH::AABox& inWorldSpaceBounds, float inLODScaleSq, JPH::ColorArg inModelColor, const GeometryRef& inGeometry, ECullMode inCullMode, ECastShadow inCastShadow, EDrawMode inDrawMode)
        {
            // Figure out which LOD to use
            const LOD* lod = inGeometry->mLODs.data();

            // Draw the batch
            const BatchImpl* batch = static_cast<const BatchImpl*>(lod->mTriangleBatch.GetPtr());
            const DebugArrayTriangle triangleArray = batch->mTriangles;

            DrawMesh(inModelMatrix, triangleArray, inModelColor, inCullMode, inDrawMode);
        }

        virtual Batch CreateTriangleBatch(const DebugRendererTriangle*inTriangles, int inTriangleCount)
        {
            BatchImpl *batch = new BatchImpl;
            if (inTriangles == nullptr || inTriangleCount == 0)
                return batch;

            batch->mTriangles.assign(inTriangles, inTriangles + inTriangleCount);
            return batch;
        }

        virtual Batch CreateTriangleBatch(const Vertex* inVertices, int inVertexCount, const JPH::uint32* inIndices, int inIndexCount)
        {
            BatchImpl* batch = new BatchImpl;
            if (inVertices == nullptr || inVertexCount == 0 || inIndices == nullptr || inIndexCount == 0)
                return batch;

            // Convert indexed triangle list to triangle list
            batch->mTriangles.resize(inIndexCount / 3);
            for (size_t t = 0; t < batch->mTriangles.size(); ++t)
            {
                DebugRendererTriangle& triangle = batch->mTriangles[t];
                triangle.mV[0] = inVertices[inIndices[t * 3 + 0]];
                triangle.mV[1] = inVertices[inIndices[t * 3 + 1]];
                triangle.mV[2] = inVertices[inIndices[t * 3 + 2]];
            }

            return batch;
        }

        virtual void DrawLine(const JPH::RVec3 *inFrom, const JPH::RVec3 *inTo, const JPH::Color *inColor) = 0;

        virtual void DrawLine(JPH::RVec3Arg inFrom, JPH::RVec3Arg inTo, JPH::ColorArg inColor)
        {
            DrawLine(&inFrom, &inTo, &inColor);
        }

        virtual void DrawTriangle(const JPH::RVec3 *inV1, const JPH::RVec3 *inV2, const JPH::RVec3 *inV3, const JPH::Color *inColor, ECastShadow inCastShadow = ECastShadow::Off) = 0;

        virtual void DrawTriangle(JPH::RVec3Arg inV1, JPH::RVec3Arg inV2, JPH::RVec3Arg inV3, JPH::ColorArg inColor, ECastShadow inCastShadow = ECastShadow::Off)
        {
            DrawTriangle(&inV1, &inV2, &inV3, &inColor, inCastShadow);
        }

        virtual void DrawText3D(const JPH::RVec3 *inPosition, const void *inString, JPH::uint32 inStringLen, const JPH::Color *inColor, float inHeight) = 0;

        virtual void DrawText3D(JPH::RVec3Arg inPosition, const string_view &inString, JPH::ColorArg inColor, float inHeight)
        {
            DrawText3D(&inPosition, (const void*)inString.data(), inString.size(), &inColor, inHeight);
        }

private:
    /// Implementation specific batch object
    class BatchImpl : public JPH::RefTargetVirtual
    {
    public:
        JPH_OVERRIDE_NEW_DELETE

            virtual void AddRef() override { ++mRefCount; }
        virtual void Release() override { if (--mRefCount == 0) delete this; }

        DebugArrayTriangle mTriangles;

    private:
        atomic<JPH::uint32> mRefCount = 0;
    };

};
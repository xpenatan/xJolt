package jolt.example.samples.app.tests.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import jolt.JoltNew;
import jolt.JoltTemp;
import jolt.enums.EActivation;
import jolt.enums.EMotionType;
import jolt.example.samples.app.jolt.Layers;
import jolt.example.samples.app.tests.Test;
import jolt.geometry.Plane;
import jolt.math.Mat44;
import jolt.math.Quat;
import jolt.math.Vec3;
import jolt.physics.PhysicsSystem;
import jolt.physics.body.BodyCreationSettings;
import jolt.physics.body.BodyID;
import jolt.physics.character.CharacterContactListener;
import jolt.physics.character.CharacterVirtual;
import jolt.physics.character.CharacterVirtualSettings;
import jolt.physics.collision.shape.CapsuleShape;
import jolt.physics.collision.shape.CylinderShape;
import jolt.physics.collision.shape.RotatedTranslatedShapeSettings;
import jolt.physics.collision.shape.StaticCompoundShapeSettings;

public class CharacterSpaceShipTest extends Test {

    static float cCharacterHeightStanding = 1.35f;
    static float cCharacterRadiusStanding = 0.3f;
    static float cCharacterSpeed = 6.0f;
    static float cJumpSpeed = 4.0f;

    private CharacterVirtual mCharacter;


    // The space ship
    private BodyID mSpaceShip;

    // Previous frame space ship transform
    private Mat44 mSpaceShipPrevTransform;

    // Space ship velocity
    private Vec3 mSpaceShipLinearVelocity;
    private Vec3 mSpaceShipAngularVelocity;

    // Global time
    float mTime = 0.0f;

    @Override
    public void initialize() {
        mSpaceShipLinearVelocity = JoltNew.Vec3();
        mSpaceShipAngularVelocity = JoltNew.Vec3();

        float cSpaceShipHeight = 2.0f;
        float cSpaceShipRingHeight = 0.2f;
        float cSpaceShipRadius = 100.0f;
        Vec3 cShipInitialPosition = JoltTemp.Vec3_1(-25, 15, 0);

        // Create floor for reference
        createFloor();

        // Create 'player' character
        CharacterVirtualSettings settings = new CharacterVirtualSettings();
        Vec3 inPosition = JoltTemp.Vec3_2(0, 0.5f * cCharacterHeightStanding + cCharacterRadiusStanding, 0);
        CapsuleShape capsuleShape = new CapsuleShape(0.5f * cCharacterHeightStanding, cCharacterRadiusStanding);
        RotatedTranslatedShapeSettings shapeSettings = JoltNew.RotatedTranslatedShapeSettings(inPosition, Quat.sIdentity(), capsuleShape);
        settings.set_mShape(shapeSettings.Create().Get());
        settings.set_mSupportingVolume(new Plane(Vec3.sAxisY(), -cCharacterRadiusStanding)); // Accept contacts that touch the lower sphere of the capsule
        Vec3 position = cShipInitialPosition.Add(JoltTemp.Vec3_3(0, cSpaceShipHeight, 0));
        mCharacter = new CharacterVirtual(settings, position, Quat.sIdentity(), mPhysicsSystem);
        mCharacter.SetListener(new CharacterContactListener() {


        });

        // Create the space ship
        StaticCompoundShapeSettings compound = new StaticCompoundShapeSettings();
//        compound.SetEmbedded();
        for(float h = cSpaceShipRingHeight; h < cSpaceShipHeight; h += cSpaceShipRingHeight) {
            float val = cSpaceShipRadius - cSpaceShipHeight - cSpaceShipRingHeight + h;
            float radius = (float)Math.sqrt((cSpaceShipRadius * cSpaceShipRadius) - (val * val));
            compound.AddShape(Vec3.sZero(), Quat.sIdentity(), new CylinderShape(h, radius));
        }
        BodyCreationSettings bodyCreationSettings = JoltNew.BodyCreationSettings(compound, cShipInitialPosition, Quat.sIdentity(), EMotionType.Kinematic, Layers.MOVING);
        mSpaceShip = mBodyInterface.CreateAndAddBody(bodyCreationSettings, EActivation.Activate);
        mSpaceShipPrevTransform = mBodyInterface.GetCenterOfMassTransform(mSpaceShip);
    }

    @Override
    public void prePhysicsUpdate(boolean isPlaying) {

        if(!isPlaying) {
            return;
        }

        mTime += Gdx.graphics.getDeltaTime();


        updateShipVelocity();
    }

    void updateShipVelocity() {
        // Make it a rocky ride...
        mSpaceShipLinearVelocity.SetVec3(JoltTemp.Vec3_1(MathUtils.sin(mTime), 0, MathUtils.cos(mTime)).MulFloat(50.0f));
        mSpaceShipAngularVelocity.SetVec3(JoltTemp.Vec3_2(MathUtils.sin(2.0f * mTime), 1, MathUtils.cos(2.0f * mTime)).MulFloat(0.5f));

        mBodyInterface.SetLinearAndAngularVelocity(mSpaceShip, mSpaceShipLinearVelocity, mSpaceShipAngularVelocity);
    }

    @Override
    public void dispose() {
        mSpaceShipLinearVelocity.dispose();
        mSpaceShipAngularVelocity.dispose();
    }
}
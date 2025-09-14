package jolt.example.samples.app.tests.character;

import jolt.JoltNew;
import jolt.JoltTemp;
import jolt.example.samples.app.tests.Test;
import jolt.geometry.Plane;
import jolt.math.Quat;
import jolt.math.Vec3;
import jolt.physics.PhysicsSystem;
import jolt.physics.character.CharacterContactListener;
import jolt.physics.character.CharacterVirtual;
import jolt.physics.character.CharacterVirtualSettings;
import jolt.physics.collision.shape.CapsuleShape;
import jolt.physics.collision.shape.RotatedTranslatedShapeSettings;
import jolt.physics.collision.shape.StaticCompoundShapeSettings;

public class CharacterSpaceShipTest extends Test {

    static float cCharacterHeightStanding = 1.35f;
    static float cCharacterRadiusStanding = 0.3f;
    static float cCharacterSpeed = 6.0f;
    static float cJumpSpeed = 4.0f;

    private CharacterVirtual mCharacter;

    @Override
    public void initialize() {
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
        compound.SetEmbedded();
    }
}
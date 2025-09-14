package jolt.example.samples.app.tests.character;

import jolt.JoltNew;
import jolt.example.samples.app.tests.Test;
import jolt.geometry.Plane;
import jolt.math.Quat;
import jolt.math.Vec3;
import jolt.physics.character.CharacterContactListener;
import jolt.physics.character.CharacterVirtual;
import jolt.physics.character.CharacterVirtualSettings;
import jolt.physics.collision.shape.CapsuleShape;
import jolt.physics.collision.shape.RotatedTranslatedShapeSettings;

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
	    Vec3 cShipInitialPosition = JoltNew.Vec3(-25, 15, 0);

        // Create floor for reference
        createFloor();

        // Create 'player' character
//        CharacterVirtualSettings settings = new CharacterVirtualSettings();
//        Vec3 inPosition = JoltNew.Vec3(0, 0.5f * cCharacterHeightStanding + cCharacterRadiusStanding, 0);
//        CapsuleShape capsuleShape = new CapsuleShape(0.5f * cCharacterHeightStanding, cCharacterRadiusStanding);
//        RotatedTranslatedShapeSettings shapeSettings = JoltNew.RotatedTranslatedShapeSettings(inPosition, Quat.sIdentity(), capsuleShape);
//        settings.set_mShape(shapeSettings.Create().Get());
//        settings.set_mSupportingVolume(new Plane(Vec3.sAxisY(), -cCharacterRadiusStanding)); // Accept contacts that touch the lower sphere of the capsule
//        mCharacter = new CharacterVirtual(settings, cShipInitialPosition.Add(JoltNew.Vec3(0, cSpaceShipHeight, 0), Quat::sIdentity(), 0, mPhysicsSystem);
//        mCharacter.SetListener(new CharacterContactListener() {
//        });

    }
}
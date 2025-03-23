package jolt.example.samples.app.tests.playground;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import imgui.ImGui;
import imgui.idl.helper.IDLInt;
import jolt.Jolt;
import jolt.enums.EActivation;
import jolt.enums.EMotionType;
import jolt.example.samples.app.jolt.Layers;
import jolt.example.samples.app.tests.Test;
import jolt.math.Quat;
import jolt.math.Vec3;
import jolt.physics.body.Body;
import jolt.physics.body.BodyCreationSettings;
import jolt.physics.body.BodyID;
import jolt.physics.body.BodyInterface;
import jolt.physics.collision.shape.BoxShape;

public class BoxSpawnTest extends Test {

    private int resetDelaySeconds = 8;

    private long timeNow;
    private long time;

    private boolean wasPlaying;
    private int totalCubes = 3000;
    private int cubeCount = 0;

    private Array<Body> bodies = new Array<>();

    private Vec3 tempVec3;
    private Quat tempQuat;
    private Quaternion tempQuaternion;
    private Matrix4 tempRotationMatrix;

    public void initialize() {
        tempVec3 = Jolt.New_Vec3();
        tempQuat = new Quat();
        tempQuaternion = new Quaternion();
        tempRotationMatrix = new Matrix4();
        createFloor();
        resetBoxes();
    }

    @Override
    public void prePhysicsUpdate(boolean isPlaying) {
        if(isPlaying) {
            if(!wasPlaying) {
                time = System.currentTimeMillis() + (time - timeNow);
            }
            timeNow = System.currentTimeMillis();
            long timeout = resetDelaySeconds * 1000;
            if(timeNow - time > timeout) {
                resetBoxes();
                time = System.currentTimeMillis();
            }
        }
        wasPlaying = isPlaying;
    }

    private void resetBoxes() {
        BodyInterface bodyInterface = mPhysicsSystem.GetBodyInterface();
        for(Body body : bodies) {
            BodyID bodyID = body.GetID();
            bodyInterface.RemoveBody(bodyID);
            bodyInterface.DestroyBody(bodyID);
        }
        bodies.clear();
        mPhysicsSystem.OptimizeBroadPhase();

        // Step 1: Initialize dimensions safely
        int maxX = 1;
        int maxY = 1;
        int maxZ = 1;

        int base = (int) Math.round(Math.cbrt(totalCubes));
        maxX = base;
        maxY = base;
        maxZ = base;

        float multi = 1.3f;
        int offsetY = 20;
        int offsetX = -3;
        cubeCount = 0;
        for(int i = 0; i < maxX; i++) {
            for(int j = 0; j < maxY; j++) {
                for(int k = 0; k < maxZ; k++) {
                    if(cubeCount < totalCubes) {
                        float x = (i + offsetX) * multi;
                        float y = (j + offsetY) * multi;
                        float z = k * multi;
                        float axisX = MathUtils.random(0, 360);
                        float axisY = MathUtils.random(0, 360);
                        float axisZ = MathUtils.random(0, 360);
                        float r = 1f;
                        float g = 1f;
                        float b = 1f;
                        createBox("ID: " + cubeCount, true, 0.4f, x, y, z, axisX, axisY, axisZ, 1, 1, 1, r, g, b);
                        cubeCount++;

                        if(i == maxX-1 && j == maxY-1 && k == maxZ-1) {
                            if(cubeCount != totalCubes) {
                                maxX++;
                            }
                        }
                    }
                    else {
                        return;
                    }
                }
            }
        }
    }


    private void createBox(String userData, boolean add, float mass, float x, float y, float z, float axiX, float axiY, float axiZ, float x1, float y1, float z1, float colorR, float colorG, float colorB) {
        tempVec3.Set(x1 / 2f, y1 / 2f, z1 / 2f);
        BoxShape bodyShape = new BoxShape(tempVec3);

        tempRotationMatrix.idt();
        tempRotationMatrix.rotate(Vector3.X, axiX);
        tempRotationMatrix.rotate(Vector3.Y, axiY);
        tempRotationMatrix.rotate(Vector3.Z, axiZ);
        tempRotationMatrix.getRotation(tempQuaternion);

        tempVec3.Set(x, y, z);
        tempQuat.Set(tempQuaternion.x, tempQuaternion.y, tempQuaternion.z, tempQuaternion.w);
        BodyCreationSettings bodySettings = Jolt.New_BodyCreationSettings(bodyShape, tempVec3, tempQuat, EMotionType.Dynamic, Layers.MOVING);
        Body body = mBodyInterface.CreateBody(bodySettings);
        bodySettings.dispose();
        bodies.add(body);
        mBodyInterface.AddBody(body.GetID(), EActivation.Activate);
    }

    @Override
    public void dispose() {
        super.dispose();
        tempVec3.dispose();
        tempQuat.dispose();
    }

    @Override
    public void renderUI() {
        IDLInt.TMP_1.set(resetDelaySeconds);
        if(ImGui.SliderInt("Delay Seconds", IDLInt.TMP_1, 1, 20)) {
            resetDelaySeconds = IDLInt.TMP_1.getValue();
        }
        IDLInt.TMP_1.set(totalCubes);
        if(ImGui.SliderInt("Max Cubes", IDLInt.TMP_1, 9, 4000)) {
            totalCubes = IDLInt.TMP_1.getValue();
        }
        ImGui.Text("Cubes: " + cubeCount);
    }
}
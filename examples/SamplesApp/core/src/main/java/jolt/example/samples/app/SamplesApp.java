package jolt.example.samples.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import jolt.BodyIDVector;
import jolt.BodyManagerDrawSettings;
import jolt.example.samples.app.jolt.JoltInstance;

public class SamplesApp extends InputAdapter {
    private boolean isPaused;

    private Test test;

    private DebugRenderer debugRenderer;
    private BodyManagerDrawSettings debugSettings;

    private PerspectiveCamera camera;
    private ScreenViewport viewport;
    private CameraInputController cameraController;
    private Tests tests;

    private JoltInstance joltInstance;

    public void setup() {
        tests = new Tests();

        joltInstance = new JoltInstance();
        debugRenderer = new DebugRenderer();
        debugSettings = new BodyManagerDrawSettings();
//        debugSettings.set_mDrawShapeWireframe(true);
//        debugSettings.set_mDrawShapeColor(EShapeColor.EShapeColor_SleepColor);

        camera = new PerspectiveCamera();
        viewport = new ScreenViewport(camera);
        camera.far = 1000f;
        camera.position.set(30, 10, 30);
        camera.lookAt(0, 0, 0);

        cameraController = new CameraInputController(camera);
        cameraController.autoUpdate = false;
        cameraController.forwardTarget = false;
        cameraController.translateTarget = false;
        Gdx.input.setInputProcessor(new InputMultiplexer(this, cameraController));
    }

    public Array<Class<?>> getAllTests() {
        return tests.getAllTests();
    }

    public void render(float delta) {
        // Don't go below 30 Hz to prevent spiral of death
        float deltaTime = (float)Math.min(delta, 1.0 / 30.0);
        if(test != null) {
            if(!isPaused) {
                test.processInput();
            }
            test.updateCamera(camera);
        }
        DrawPhysics();
        if(deltaTime > 0) {
            StepPhysics(deltaTime);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            debugRenderer.setEnable(!debugRenderer.isEnable());
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            boolean mDrawShapeWireframe = debugSettings.get_mDrawShapeWireframe();
            debugSettings.set_mDrawShapeWireframe(!mDrawShapeWireframe);
        }
    }

    public void startTest(Class<? extends Test> testClass) {
        if(test != null) {
            test.dispose();
            test = null;
        }
        clearBodies();
        isPaused = true;
        test = tests.getTest(testClass);
        test.setPhysicsSystem(joltInstance.getPhysicsSystem());
        test.setDebugRenderer(debugRenderer);
        test.initialize();
        test.initializeCamera(camera);
    }

    private void DrawPhysics() {
        camera.update();
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        debugRenderer.begin(camera);
        debugRenderer.DrawBodies(joltInstance.getPhysicsSystem(), debugSettings);
        debugRenderer.end();
    }

    public void StepPhysics(float deltaTime) {
        // When running below 55 Hz, do 2 steps instead of 1
        var numSteps = deltaTime > 1.0 / 55.0 ? 2 : 1;
        boolean isPlaying = !isPaused;
        if(test != null) {
            test.prePhysicsUpdate(isPlaying);
        }
        if(isPlaying) {
            joltInstance.update(deltaTime, numSteps);
        }
        if(test != null) {
            test.postPhysicsUpdate(isPlaying, deltaTime);
        }
    }

    public void dispose() {
        clearBodies();
        debugRenderer.dispose();
        debugSettings.dispose();
        joltInstance.dispose();
    }

    private void clearBodies() {
        joltInstance.clearWorld();
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.P) {
            isPaused = !isPaused;
            return true;
        }
        if(keycode == Input.Keys.R) {
            if(test != null) {
                Class<? extends Test> aClass = test.getClass();
                startTest(aClass);
            }
            return true;
        }
        return false;
    }
}
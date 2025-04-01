package jolt.example.samples.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import imgui.ImGui;
import imgui.ImGuiCond;
import imgui.ImGuiTabBarFlags;
import imgui.ImVec2;
import jolt.example.samples.app.imgui.FPSRenderer;
import jolt.example.samples.app.imgui.ImGuiSettingsRenderer;
import jolt.example.samples.app.jolt.JoltInstance;
import jolt.example.samples.app.tests.Test;
import jolt.example.samples.app.tests.TestGroup;
import jolt.example.samples.app.tests.Tests;
import jolt.gdx.DebugRenderer;
import jolt.physics.body.BodyManagerDrawSettings;

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

    private ImGuiSettingsRenderer settingsRenderer;
    private TestGroup allTests;

    private FPSRenderer fpsRenderer;

    public void setup(InputMultiplexer input) {
        fpsRenderer = new FPSRenderer();
        tests = new Tests();
        allTests = tests.getAllTests();

        settingsRenderer = new ImGuiSettingsRenderer();

        joltInstance = new JoltInstance();
        debugRenderer = new DebugRenderer();
        debugSettings = new BodyManagerDrawSettings();

        camera = new PerspectiveCamera();
        viewport = new ScreenViewport(camera);
        camera.far = 1000f;

        cameraController = new CameraInputController(camera);
        cameraController.autoUpdate = false;
        cameraController.forwardTarget = false;
        cameraController.translateTarget = false;

        input.addProcessor(this);
        input.addProcessor(cameraController);
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
    }

    public void renderUI() {
        Class<Test> newTest = null;
        ImGui.SetNextWindowSize(ImVec2.TMP_1.set(250, 400), ImGuiCond.FirstUseEver);
        ImGui.Begin("Settings");

        fpsRenderer.render();

        newTest = settingsRenderer.render(allTests);

        settingsRenderer.idlBool.set(isPaused);
        if(ImGui.Checkbox("IsPaused", settingsRenderer.idlBool)) {
            isPaused = settingsRenderer.idlBool.getValue();
        }

        settingsRenderer.idlBool.set(debugRenderer.isEnable());
        if(ImGui.Checkbox("DebugRenderer", settingsRenderer.idlBool)) {
            debugRenderer.setEnable(settingsRenderer.idlBool.getValue());
        }

        if(ImGui.BeginTabBar("##Settings", ImGuiTabBarFlags.FittingPolicyScroll | ImGuiTabBarFlags.Reorderable)) {
            if(ImGui.BeginTabItem("Physics")) {
                settingsRenderer.render(joltInstance);
                ImGui.EndTabItem();
            }
            if(ImGui.BeginTabItem("DebugRenderer")) {
                settingsRenderer.render(debugSettings);
                ImGui.EndTabItem();
            }
            if(ImGui.BeginTabItem("Test")) {
                test.renderUI();
                ImGui.EndTabItem();
            }
            ImGui.EndTabBar();
        }
        ImGui.End();

        if(newTest != null) {
            startTest(newTest);
        }
    }

    public void startTest(Class<? extends Test> testClass) {
        if(test != null) {
            test.dispose();
            test = null;
        }
        clearBodies();
        isPaused = true;
        camera.up.set(0, 1, 0);
        camera.position.set(30, 10, 30);
        camera.lookAt(0, 0, 0);
        test = tests.getTest(testClass);
        test.setPhysicsSystem(joltInstance.getPhysicsSystem());
        test.setDebugRenderer(debugRenderer);
        test.initializeCamera(camera);
        test.initialize();
        test.processInput();
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
        fpsRenderer.dispose();
        settingsRenderer.dispose();
        clearBodies();
        debugRenderer.dispose();
        debugSettings.dispose();
        joltInstance.dispose();
    }

    private void clearBodies() {
        debugRenderer.clear();
        joltInstance.clearWorld();
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.P) {
            isPaused = !isPaused;
            return true;
        }
        else if(keycode == Input.Keys.R) {
            if(test != null) {
                Class<? extends Test> aClass = test.getClass();
                startTest(aClass);
            }
            return true;
        }
        else if(keycode == Input.Keys.D) {
            debugRenderer.setEnable(!debugRenderer.isEnable());
        }
        else if(keycode == Input.Keys.W) {
            boolean mDrawShapeWireframe = debugSettings.get_mDrawShapeWireframe();
            debugSettings.set_mDrawShapeWireframe(!mDrawShapeWireframe);
        }
        return false;
    }
}
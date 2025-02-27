package jolt.example.samples.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import jolt.BodyIDVector;
import jolt.BodyManagerDrawSettings;
import jolt.EShapeColor;
import jolt.JoltInterface;
import jolt.JoltSettings;
import jolt.jolt.physics.PhysicsSystem;
import jolt.jolt.physics.body.BodyID;
import jolt.jolt.physics.body.BodyInterface;
import jolt.jolt.physics.collision.ObjectLayerPairFilterTable;
import jolt.jolt.physics.collision.broadphase.BroadPhaseLayer;
import jolt.jolt.physics.collision.broadphase.BroadPhaseLayerInterfaceTable;
import jolt.jolt.physics.collision.broadphase.ObjectVsBroadPhaseLayerFilterTable;

public class SamplesApp extends InputAdapter {
    // Object layers
    public static int LAYER_NON_MOVING = 0;
    public static int LAYER_MOVING = 1;
    public static int NUM_OBJECT_LAYERS = 2;

    private boolean isPaused;

    private Test test;

    private JoltInterface jolt;
    private PhysicsSystem  physicsSystem;
    private DebugRenderer debugRenderer;
    private BodyManagerDrawSettings debugSettings;
    private BodyIDVector bodyIDVector;

    private PerspectiveCamera camera;
    private ScreenViewport viewport;
    private CameraInputController cameraController;
    private Tests tests;

    public void setup() {
        tests = new Tests();
        JoltSettings settings = new JoltSettings();
        setupCollisionFiltering(settings);
        jolt = new JoltInterface(settings);
        settings.dispose();
        physicsSystem = jolt.GetPhysicsSystem();
        debugRenderer = new DebugRenderer();
        debugSettings = new BodyManagerDrawSettings();
//        debugSettings.set_mDrawShapeWireframe(true);
//        debugSettings.set_mDrawShapeColor(EShapeColor.EShapeColor_SleepColor);
        bodyIDVector = new BodyIDVector();

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
        ScreenUtils.clear(0.2f, 0.2f, 0.2f, 1, true);
        // Don't go below 30 Hz to prevent spiral of death
        float deltaTime = (float)Math.min(delta, 1.0 / 30.0);

        if(test != null) {
            test.update(deltaTime);
        }
        DrawPhysics();
        StepPhysics(deltaTime);
    }

    public void startTest(Class<? extends Test> testClass) {
        if(test != null) {
            test.dispose();
            test = null;
        }
        clearBodies();
        isPaused = true;
        test = tests.getTest(testClass);
        test.setPhysicsSystem(physicsSystem);
        test.initialize();
    }

    private void DrawPhysics() {
        camera.update();
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        debugRenderer.begin(camera);
        debugRenderer.DrawBodies(physicsSystem, debugSettings);
        debugRenderer.end();
    }

    public void StepPhysics(float deltaTime) {
        // When running below 55 Hz, do 2 steps instead of 1
        var numSteps = deltaTime > 1.0 / 55.0 ? 2 : 1;

        if(!isPaused) {
            jolt.Step(deltaTime, numSteps);
        }
    }

    public void dispose() {
        debugRenderer.dispose();
        physicsSystem.dispose();
        debugSettings.dispose();
    }

    private void setupCollisionFiltering(JoltSettings settings) {
        // Layer that objects can be in, determines which other objects it can collide with
        // Typically you at least want to have 1 layer for moving bodies and 1 layer for static bodies, but you can have more
        // layers if you want. E.g. you could have a layer for high detail collision (which is not used by the physics simulation
        // but only if you do collision testing).

        ObjectLayerPairFilterTable objectFilter = new ObjectLayerPairFilterTable(NUM_OBJECT_LAYERS);
        objectFilter.EnableCollision(LAYER_NON_MOVING, LAYER_MOVING);
        objectFilter.EnableCollision(LAYER_MOVING, LAYER_MOVING);

        // Each broadphase layer results in a separate bounding volume tree in the broad phase. You at least want to have
        // a layer for non-moving and moving objects to avoid having to update a tree full of static objects every frame.
        // You can have a 1-on-1 mapping between object layers and broadphase layers (like in this case) but if you have
        // many object layers you'll be creating many broad phase trees, which is not efficient.

        BroadPhaseLayer BP_LAYER_NON_MOVING = new BroadPhaseLayer((short)0);
        BroadPhaseLayer BP_LAYER_MOVING = new BroadPhaseLayer((short)1);
        int NUM_BROAD_PHASE_LAYERS = 2;
        BroadPhaseLayerInterfaceTable bpInterface = new BroadPhaseLayerInterfaceTable(NUM_OBJECT_LAYERS, NUM_BROAD_PHASE_LAYERS);
        bpInterface.MapObjectToBroadPhaseLayer(LAYER_NON_MOVING, BP_LAYER_NON_MOVING);
        bpInterface.MapObjectToBroadPhaseLayer(LAYER_MOVING, BP_LAYER_MOVING);

        settings.set_mObjectLayerPairFilter(objectFilter);
        settings.set_mBroadPhaseLayerInterface(bpInterface);
        ObjectVsBroadPhaseLayerFilterTable broadPhaseLayerFilter = new ObjectVsBroadPhaseLayerFilterTable(settings.get_mBroadPhaseLayerInterface(), NUM_BROAD_PHASE_LAYERS, settings.get_mObjectLayerPairFilter(), NUM_OBJECT_LAYERS);
        settings.set_mObjectVsBroadPhaseLayerFilter(broadPhaseLayerFilter);
    }

    private void clearBodies() {
        BodyInterface bodyInterface  = physicsSystem.GetBodyInterface();
        physicsSystem.GetBodies(bodyIDVector);
        int size = bodyIDVector.size();
        for(int i = 0; i < size; i++) {
            BodyID bodyId = bodyIDVector.at(i);
            bodyInterface.RemoveBody(bodyId);
            bodyInterface.DestroyBody(bodyId);
        }
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
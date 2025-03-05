package jolt.example.samples.app;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.utils.ScreenUtils;
import jolt.example.samples.app.tests.vehicle.VehicleConstraintTest;

public class GameScreen extends ScreenAdapter {

    private SamplesApp samplesApp;
    private FPSLogger fpsLogger;

    @Override
    public void show() {
        samplesApp = new SamplesApp();
        samplesApp.setup();
        samplesApp.startTest(VehicleConstraintTest.class);
        fpsLogger = new FPSLogger();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.1f, 0.8f, 1, true);
        samplesApp.render(delta);
        fpsLogger.log();
    }

    @Override
    public void dispose() {
        samplesApp.dispose();
    }
}
package jolt.example.samples.app;

import com.badlogic.gdx.ScreenAdapter;
import jolt.example.samples.app.tests.BoxShapeTest;
import jolt.example.samples.app.tests.vehicle.VehicleConstraintTest;
import jolt.example.samples.app.tests.vehicle.VehicleTest;

public class GameScreen extends ScreenAdapter {

    private SamplesApp samplesApp;

    @Override
    public void show() {
        samplesApp = new SamplesApp();
        samplesApp.setup();
        samplesApp.startTest(VehicleConstraintTest.class);
    }

    @Override
    public void render(float delta) {
        samplesApp.render(delta);
    }

    @Override
    public void dispose() {
        samplesApp.dispose();
    }
}
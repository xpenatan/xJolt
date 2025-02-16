package jolt.example.samples.app;

import com.badlogic.gdx.ScreenAdapter;
import jolt.example.samples.app.tests.BoxShapeTest;

public class GameScreen extends ScreenAdapter {

    private SamplesApp samplesApp;

    @Override
    public void show() {
        samplesApp = new SamplesApp();
        samplesApp.setup();
        samplesApp.startTest(BoxShapeTest.class);
    }

    @Override
    public void render(float delta) {
        samplesApp.update(delta);
    }
}
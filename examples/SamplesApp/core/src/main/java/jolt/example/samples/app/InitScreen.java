package jolt.example.samples.app;

import com.badlogic.gdx.ScreenAdapter;
import jolt.JoltLoader;

public class InitScreen extends ScreenAdapter {

    private JoltGame game;

    private boolean init = false;

    public InitScreen(JoltGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        JoltLoader.init((isSuccess, e) -> init = isSuccess);
    }

    @Override
    public void render(float delta) {
        if(init) {
            init = false;
            game.setScreen(new GameScreen());
        }
    }
}

package jolt.example.samples.app;

import com.github.xpenatan.gdx.backends.teavm.TeaApplication;
import com.github.xpenatan.gdx.backends.teavm.TeaApplicationConfiguration;
import jolt.example.graphics.GdxGraphicApi;
import jolt.example.graphics.GraphicManagerApi;

public class Launcher {

    public static void main(String[] args) {
        GraphicManagerApi.graphicApi = new GdxGraphicApi();

        TeaApplicationConfiguration config = new TeaApplicationConfiguration("canvas");
        config.useDebugGL = false;
        config.width = 0;
        config.height = 0;
        config.useGL30 = true;
        new TeaApplication(new JoltGame(), config);
    }
}
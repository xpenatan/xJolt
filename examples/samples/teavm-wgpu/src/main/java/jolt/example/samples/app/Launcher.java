package jolt.example.samples.app;

import com.github.xpenatan.gdx.backends.teavm.TeaApplicationConfiguration;
import com.monstrous.gdx.webgpu.backends.teavm.WgTeaApplication;
import jolt.gdx.GraphicManagerApi;
import jolt.gdx.WGPUGraphicApi;

public class Launcher {

    public static void main(String[] args) {
        GraphicManagerApi.graphicApi = new WGPUGraphicApi();

        TeaApplicationConfiguration config = new TeaApplicationConfiguration("canvas");
        config.width = 0;
        config.height = 0;
        new WgTeaApplication(new JoltGame(), config);
    }
}
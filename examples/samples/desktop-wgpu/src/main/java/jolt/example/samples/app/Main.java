package jolt.example.samples.app;

import com.github.xpenatan.webgpu.JWebGPUBackend;
import com.monstrous.gdx.webgpu.backends.desktop.WgDesktopApplication;
import com.monstrous.gdx.webgpu.backends.desktop.WgDesktopApplicationConfiguration;
import jolt.gdx.GraphicManagerApi;
import jolt.gdx.WGPUGraphicApi;

public class Main {
    public static void main(String[] args) {
        GraphicManagerApi.graphicApi = new WGPUGraphicApi();

        WgDesktopApplicationConfiguration config = new WgDesktopApplicationConfiguration();
        config.setWindowedMode(640, 480);
        config.setTitle("WebGPU");
        config.enableGPUtiming = false;
        config.useVsync(false);
        config.backendWebGPU = JWebGPUBackend.DAWN;
        new WgDesktopApplication(new JoltGame(), config);
    }
}
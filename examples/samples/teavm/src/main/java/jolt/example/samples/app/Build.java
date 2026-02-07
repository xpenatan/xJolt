package jolt.example.samples.app;

import com.github.xpenatan.gdx.teavm.backends.shared.config.AssetFileHandle;
import com.github.xpenatan.gdx.teavm.backends.shared.config.compiler.TeaCompiler;
import com.github.xpenatan.gdx.teavm.backends.web.config.backend.WebBackend;
import java.io.File;
import org.teavm.vm.TeaVMOptimizationLevel;

public class Build {

    public static void main(String[] args) {
        AssetFileHandle assetsPath = new AssetFileHandle("../assets");
        new TeaCompiler(new WebBackend()
                .setStartJettyAfterBuild(true)
                .setWebAssembly(false))
                .addAssets(assetsPath)
                .setOptimizationLevel(TeaVMOptimizationLevel.SIMPLE)
                .setMainClass(Launcher.class.getName())
                .setObfuscated(false)
                .build(new File("build/dist"));
    }
}

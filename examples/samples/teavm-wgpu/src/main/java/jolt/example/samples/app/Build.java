package jolt.example.samples.app;

import com.github.xpenatan.gdx.backends.teavm.config.AssetFileHandle;
import com.github.xpenatan.gdx.backends.teavm.config.TeaBuildConfiguration;
import com.github.xpenatan.gdx.backends.teavm.config.TeaBuilder;
import com.github.xpenatan.gdx.backends.teavm.config.TeaTargetType;
import java.io.File;
import java.io.IOException;
import org.teavm.tooling.TeaVMSourceFilePolicy;
import org.teavm.tooling.TeaVMTargetType;
import org.teavm.tooling.TeaVMTool;
import org.teavm.tooling.sources.DirectorySourceFileProvider;
import org.teavm.tooling.sources.JarSourceFileProvider;
import org.teavm.vm.TeaVMOptimizationLevel;

public class Build {

    public static void main(String[] args) throws IOException {
        TeaBuildConfiguration teaBuildConfiguration = new TeaBuildConfiguration();
        teaBuildConfiguration.assetsPath.add(new AssetFileHandle("../assets"));
        teaBuildConfiguration.webappPath = new File("build/dist").getCanonicalPath();
        teaBuildConfiguration.targetType = TeaTargetType.JAVASCRIPT;
        TeaBuilder.config(teaBuildConfiguration);

        TeaVMTool tool = new TeaVMTool();
        tool.setMainClass(Launcher.class.getName());
        tool.setOptimizationLevel(TeaVMOptimizationLevel.SIMPLE);
        tool.setObfuscated(false);

//        tool.setDebugInformationGenerated(true);
//        tool.setSourceMapsFileGenerated(true);
//        tool.setSourceFilePolicy(TeaVMSourceFilePolicy.COPY);
//        tool.addSourceFileProvider(new DirectorySourceFileProvider(new File("../core/src/main/java/")));
//        tool.addSourceFileProvider(new DirectorySourceFileProvider(new File("E:\\Dev\\Projects\\java\\gdx-webgpu\\gdx-webgpu\\src\\main\\java")));

        TeaBuilder.build(tool);
    }
}

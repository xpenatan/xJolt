package jolt.example.samples.app;

import com.github.xpenatan.gdx.backends.teavm.config.AssetFileHandle;
import com.github.xpenatan.gdx.backends.teavm.config.TeaBuildConfiguration;
import com.github.xpenatan.gdx.backends.teavm.config.TeaBuilder;
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

        TeaVMTool tool = TeaBuilder.config(teaBuildConfiguration);
        tool.setMainClass(Launcher.class.getName());
        tool.setTargetType(TeaVMTargetType.JAVASCRIPT);
        tool.setOptimizationLevel(TeaVMOptimizationLevel.SIMPLE);
        tool.setObfuscated(false);

        tool.setDebugInformationGenerated(true);
        tool.setSourceMapsFileGenerated(true);
        tool.setSourceFilePolicy(TeaVMSourceFilePolicy.COPY);
        tool.addSourceFileProvider(new DirectorySourceFileProvider(new File("../core/src/main/java/")));
        tool.addSourceFileProvider(new DirectorySourceFileProvider(new File("E:\\Dev\\Projects\\java\\gdx-webgpu\\gdx-webgpu\\src\\main\\java")));
        String classpath = System.getProperty("java.class.path");
        String[] classpathEntries = classpath.split(File.pathSeparator);
        for (String entry : classpathEntries) {
            File file = new File(entry);
            if (file.exists()) {
                if (file.isFile() && entry.endsWith("-sources.jar")) {
                    System.out.println("Source JAR in classpath: " + entry);
                    tool.addSourceFileProvider(new JarSourceFileProvider(new File(entry)));
                }
            }
        }

        TeaBuilder.build(tool);
    }
}

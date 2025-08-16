import com.github.xpenatan.jparser.builder.BuildMultiTarget;
import com.github.xpenatan.jparser.builder.targets.AndroidTarget;
import com.github.xpenatan.jparser.builder.targets.EmscriptenTarget;
import com.github.xpenatan.jparser.builder.targets.LinuxTarget;
import com.github.xpenatan.jparser.builder.targets.MacTarget;
import com.github.xpenatan.jparser.builder.targets.WindowsMSVCTarget;
import com.github.xpenatan.jparser.builder.tool.BuildToolListener;
import com.github.xpenatan.jparser.builder.tool.BuildToolOptions;
import com.github.xpenatan.jparser.builder.tool.BuilderTool;
import com.github.xpenatan.jparser.idl.IDLHelper;
import com.github.xpenatan.jparser.idl.IDLReader;
import com.github.xpenatan.jparser.idl.IDLRenaming;
import java.util.ArrayList;

public class Build {

    public static void main(String[] args) {
        String libName = "jolt";
        String modulePrefix = "jolt";
        String basePackage = "jolt";
        String sourcePath =  "/build/jolt";
//        String sourcePath =  "E:\\Dev\\Projects\\cpp\\JoltPhysics";
//        WindowsMSVCTarget.DEBUG_BUILD = true;

        IDLHelper.cppConverter = idlType -> {
            if(idlType.equals("unsigned long long")) {
                return "uint64";
            }
            return null;
        };

        BuildToolOptions op = new BuildToolOptions(libName, basePackage, modulePrefix , sourcePath, args);
        BuilderTool.build(op, new BuildToolListener() {
            @Override
            public void onAddTarget(BuildToolOptions op, IDLReader idlReader, ArrayList<BuildMultiTarget> targets) {
                if(op.containsArg("teavm")) {
                    targets.add(getTeaVMTarget(op, idlReader));
                }
                if(op.containsArg("windows64")) {
                    targets.add(getWindowTarget(op));
                }
                if(op.containsArg("linux64")) {
                    targets.add(getLinuxTarget(op));
                }
                if(op.containsArg("mac64")) {
                    targets.add(getMacTarget(op, false));
                }
                if(op.containsArg("macArm")) {
                    targets.add(getMacTarget(op, true));
                }
                if(op.containsArg("android")) {
                    targets.add(getAndroidTarget(op));
                }
//                if(op.containsArg("iOS")) {
//                    targets.add(getIOSTarget(op));
//                }
            }
        }, new IDLRenaming() {
            @Override
            public String obtainNewPackage(String className, String classPackage) {
                // This remove duplicate jolt name in package.
                // The reason for this is that the lib name start with jolt and there is an already c++ jolt subfolder
                return classPackage.replace("jolt", "");
            }
        });
    }

    private static BuildMultiTarget getWindowTarget(BuildToolOptions op) {
        BuildMultiTarget multiTarget = new BuildMultiTarget();
        String libBuildCPPPath = op.getModuleBuildCPPPath();
        String sourceDir = op.getSourceDir();

        WindowsMSVCTarget.DEBUG_BUILD = true;

        // Make a static library
        WindowsMSVCTarget windowsTarget = new WindowsMSVCTarget();
        windowsTarget.isStatic = true;
        windowsTarget.headerDirs.add("-I" + sourceDir);
        windowsTarget.cppInclude.add(sourceDir + "/Jolt/**.cpp");
        windowsTarget.cppFlags.add("-DJPH_DEBUG_RENDERER");
        windowsTarget.cppFlags.add("-DJPH_DISABLE_CUSTOM_ALLOCATOR");
        windowsTarget.cppFlags.add("-DJPH_ENABLE_ASSERTS");
        windowsTarget.cppFlags.add("-DJPH_CROSS_PLATFORM_DETERMINISTIC");
        windowsTarget.cppFlags.add("-DJPH_OBJECT_LAYER_BITS=32");
        multiTarget.add(windowsTarget);

        // Compile glue code and link
        WindowsMSVCTarget linkTarget = new WindowsMSVCTarget();
        linkTarget.addJNIHeaders();
        linkTarget.headerDirs.add("-I" + sourceDir);
        linkTarget.headerDirs.add("-I" + op.getCustomSourceDir());
        linkTarget.cppInclude.add(libBuildCPPPath + "/src/jniglue/JNIGlue.cpp");
        linkTarget.linkerFlags.add(libBuildCPPPath + "/libs/windows/vc/jolt64_.lib");
        linkTarget.cppFlags.add("-DJPH_DEBUG_RENDERER");
        linkTarget.cppFlags.add("-DJPH_DISABLE_CUSTOM_ALLOCATOR");
        linkTarget.cppFlags.add("-DJPH_ENABLE_ASSERTS");
        linkTarget.cppFlags.add("-DJPH_CROSS_PLATFORM_DETERMINISTIC");
        linkTarget.cppFlags.add("-DJPH_OBJECT_LAYER_BITS=32");
        multiTarget.add(linkTarget);

        return multiTarget;
    }

    private static BuildMultiTarget getLinuxTarget(BuildToolOptions op) {
        BuildMultiTarget multiTarget = new BuildMultiTarget();
        String libBuildCPPPath = op.getModuleBuildCPPPath();
        String sourceDir = op.getSourceDir();

        // Make a static library
        LinuxTarget linuxTarget = new LinuxTarget();
        linuxTarget.isStatic = true;
        linuxTarget.headerDirs.add("-I" + sourceDir);
        linuxTarget.cppInclude.add(sourceDir + "/Jolt/**.cpp");
        linuxTarget.cppFlags.add("-DJPH_DEBUG_RENDERER");
        linuxTarget.cppFlags.add("-DJPH_DISABLE_CUSTOM_ALLOCATOR");
        linuxTarget.cppFlags.add("-DJPH_ENABLE_ASSERTS");
        linuxTarget.cppFlags.add("-DJPH_CROSS_PLATFORM_DETERMINISTIC");
        linuxTarget.cppFlags.add("-DJPH_OBJECT_LAYER_BITS=32");
        multiTarget.add(linuxTarget);

        // Compile glue code and link
        LinuxTarget linkTarget = new LinuxTarget();
        linkTarget.addJNIHeaders();
        linkTarget.headerDirs.add("-I" + sourceDir);
        linkTarget.headerDirs.add("-I" + op.getCustomSourceDir());
        linkTarget.linkerFlags.add(libBuildCPPPath + "/libs/linux/libjolt64_.a");
        linkTarget.cppInclude.add(libBuildCPPPath + "/src/jniglue/JNIGlue.cpp");
        linkTarget.cppFlags.add("-DJPH_DEBUG_RENDERER");
        linkTarget.cppFlags.add("-DJPH_DISABLE_CUSTOM_ALLOCATOR");
        linkTarget.cppFlags.add("-DJPH_ENABLE_ASSERTS");
        linkTarget.cppFlags.add("-DJPH_CROSS_PLATFORM_DETERMINISTIC");
        linkTarget.cppFlags.add("-DJPH_OBJECT_LAYER_BITS=32");
        multiTarget.add(linkTarget);

        return multiTarget;
    }

    private static BuildMultiTarget getMacTarget(BuildToolOptions op, boolean isArm) {
        BuildMultiTarget multiTarget = new BuildMultiTarget();
        String libBuildCPPPath = op.getModuleBuildCPPPath();
        String sourceDir = op.getSourceDir();

        // Make a static library
        MacTarget macTarget = new MacTarget(isArm);
        macTarget.isStatic = true;
        macTarget.headerDirs.add("-I" + sourceDir);
        macTarget.cppInclude.add(sourceDir + "/Jolt/**.cpp");
        macTarget.cppFlags.add("-DJPH_DEBUG_RENDERER");
        macTarget.cppFlags.add("-DJPH_DISABLE_CUSTOM_ALLOCATOR");
        macTarget.cppFlags.add("-DJPH_ENABLE_ASSERTS");
        macTarget.cppFlags.add("-DJPH_CROSS_PLATFORM_DETERMINISTIC");
        macTarget.cppFlags.add("-DJPH_OBJECT_LAYER_BITS=32");
        multiTarget.add(macTarget);

        // Compile glue code and link
        MacTarget linkTarget = new MacTarget(isArm);
        linkTarget.addJNIHeaders();
        linkTarget.headerDirs.add("-I" + sourceDir);
        linkTarget.headerDirs.add("-I" + op.getCustomSourceDir());
        if(isArm) {
            linkTarget.linkerFlags.add(libBuildCPPPath + "/libs/mac/arm/libjolt64_.a");
        }
        else {
            linkTarget.linkerFlags.add(libBuildCPPPath + "/libs/mac/libjolt64_.a");
        }
        linkTarget.cppInclude.add(libBuildCPPPath + "/src/jniglue/JNIGlue.cpp");
        linkTarget.cppFlags.add("-DJPH_DEBUG_RENDERER");
        linkTarget.cppFlags.add("-DJPH_DISABLE_CUSTOM_ALLOCATOR");
        linkTarget.cppFlags.add("-DJPH_ENABLE_ASSERTS");
        linkTarget.cppFlags.add("-DJPH_CROSS_PLATFORM_DETERMINISTIC");
        linkTarget.cppFlags.add("-DJPH_OBJECT_LAYER_BITS=32");
        multiTarget.add(linkTarget);

        return multiTarget;
    }

    private static BuildMultiTarget getTeaVMTarget(BuildToolOptions op, IDLReader idlReader) {
        BuildMultiTarget multiTarget = new BuildMultiTarget();
        String libBuildCPPPath = op.getModuleBuildCPPPath();
        String sourceDir = op.getSourceDir();

        EmscriptenTarget.DEBUG_BUILD = false;

        // Make a static library
        EmscriptenTarget libTarget = new EmscriptenTarget(idlReader);
        libTarget.isStatic = true;
        libTarget.compileGlueCode = false;
        libTarget.headerDirs.add("-I" + sourceDir);
        libTarget.cppInclude.add(sourceDir + "/Jolt/**.cpp");
        libTarget.cppFlags.add("-DJPH_DEBUG_RENDERER");
        libTarget.cppFlags.add("-DJPH_DISABLE_CUSTOM_ALLOCATOR");
        libTarget.cppFlags.add("-DJPH_ENABLE_ASSERTS");
        libTarget.cppFlags.add("-DJPH_CROSS_PLATFORM_DETERMINISTIC");
        libTarget.cppFlags.add("-DJPH_OBJECT_LAYER_BITS=32");
        multiTarget.add(libTarget);

        // Compile glue code and link
        EmscriptenTarget linkTarget = new EmscriptenTarget(idlReader);
        linkTarget.headerDirs.add("-I" + sourceDir);
        linkTarget.headerDirs.add("-include" + op.getCustomSourceDir() + "JoltCustom.h");
        linkTarget.linkerFlags.add(libBuildCPPPath + "/libs/emscripten/jolt_.a");
        linkTarget.cppFlags.add("-DJPH_DEBUG_RENDERER");
        linkTarget.cppFlags.add("-DJPH_DISABLE_CUSTOM_ALLOCATOR");
        linkTarget.cppFlags.add("-DJPH_ENABLE_ASSERTS");
        linkTarget.cppFlags.add("-DJPH_CROSS_PLATFORM_DETERMINISTIC");
        linkTarget.cppFlags.add("-DJPH_OBJECT_LAYER_BITS=32");
        multiTarget.add(linkTarget);

        return multiTarget;
    }

    private static BuildMultiTarget getAndroidTarget(BuildToolOptions op) {
        BuildMultiTarget multiTarget = new BuildMultiTarget();
        String sourceDir = op.getSourceDir();
        String libBuildCPPPath = op.getModuleBuildCPPPath();

        AndroidTarget.ApiLevel apiLevel = AndroidTarget.ApiLevel.Android_10_29;
        ArrayList<AndroidTarget.Target> targets = new ArrayList<>();

        targets.add(AndroidTarget.Target.x86);
        targets.add(AndroidTarget.Target.x86_64);
        targets.add(AndroidTarget.Target.armeabi_v7a);
        targets.add(AndroidTarget.Target.arm64_v8a);

        for(int i = 0; i < targets.size(); i++) {
            AndroidTarget.Target target = targets.get(i);

            // Make a static library
            AndroidTarget androidTarget = new AndroidTarget(target, apiLevel);
            androidTarget.isStatic = true;
            androidTarget.headerDirs.add("-I" + sourceDir);
            androidTarget.cppInclude.add(sourceDir + "/Jolt/**.cpp");
            androidTarget.cppFlags.add("-DJPH_DEBUG_RENDERER");
            androidTarget.cppFlags.add("-DJPH_DISABLE_CUSTOM_ALLOCATOR");
            androidTarget.cppFlags.add("-DJPH_ENABLE_ASSERTS");
            androidTarget.cppFlags.add("-DJPH_CROSS_PLATFORM_DETERMINISTIC");
            androidTarget.cppFlags.add("-DJPH_OBJECT_LAYER_BITS=32");
            multiTarget.add(androidTarget);

            // Compile glue code and link
            AndroidTarget linkTarget = new AndroidTarget(target, apiLevel);
            linkTarget.addJNIHeaders();
            linkTarget.headerDirs.add("-I" + sourceDir);
            linkTarget.headerDirs.add("-I" + op.getCustomSourceDir());
            linkTarget.cppInclude.add(libBuildCPPPath + "/src/jniglue/JNIGlue.cpp");
            linkTarget.linkerFlags.add(libBuildCPPPath + "/libs/android/" + target.getFolder() +"/lib" + op.libName + ".a");
            linkTarget.cppFlags.add("-DJPH_DEBUG_RENDERER");
            linkTarget.cppFlags.add("-DJPH_DISABLE_CUSTOM_ALLOCATOR");
            linkTarget.cppFlags.add("-DJPH_ENABLE_ASSERTS");
            linkTarget.cppFlags.add("-DJPH_CROSS_PLATFORM_DETERMINISTIC");
            linkTarget.cppFlags.add("-DJPH_OBJECT_LAYER_BITS=32");
            linkTarget.linkerFlags.add("-Wl,-z,max-page-size=16384");
            multiTarget.add(linkTarget);
        }
        return multiTarget;
    }
}
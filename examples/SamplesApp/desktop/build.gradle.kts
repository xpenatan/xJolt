import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform

dependencies {
    implementation(project(":examples:SamplesApp:core"))

    if(LibExt.exampleUseRepoLibs) {
        implementation("com.github.xpenatan.gdx-jolt:jolt-desktop:-SNAPSHOT")
    }
    else {
        implementation(project(":jolt:jolt-desktop"))
    }

    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:${LibExt.gdxVersion}")
    implementation("com.badlogicgames.gdx:gdx-platform:${LibExt.gdxVersion}:natives-desktop")
    implementation("com.github.xpenatan.gdx-imgui:imgui-ext-desktop:${LibExt.gdxImGuiVersion}")
}

val mainClassName = "jolt.example.samples.app.Main"
val assetsDir = File("../assets");

tasks.register<JavaExec>("SamplesApp-run-desktop") {
    group = "example-desktop"
    description = "Run desktop app"
    mainClass.set(mainClassName)
    classpath = sourceSets["main"].runtimeClasspath
    workingDir = assetsDir

    if(DefaultNativePlatform.getCurrentOperatingSystem().isMacOsX) {
        jvmArgs("-XstartOnFirstThread")
    }
}
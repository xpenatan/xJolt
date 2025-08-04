import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform

dependencies {
    implementation(project(":examples:samples:core"))

    if(LibExt.exampleUseRepoLibs) {
        implementation("com.github.xpenatan.gdx-jolt:jolt-desktop:-SNAPSHOT")
        implementation("com.github.xpenatan.gdx-jolt:jolt-gdx:-SNAPSHOT")
    }
    else {
        implementation(project(":jolt:jolt-desktop"))
        implementation(project(":jolt-gdx"))
    }

    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:${LibExt.gdxVersion}")
    implementation("com.badlogicgames.gdx:gdx-platform:${LibExt.gdxVersion}:natives-desktop")
//    implementation("com.github.xpenatan.gdx-imgui:imgui-ext-desktop:${LibExt.gdxImGuiVersion}")
}

val mainClassName = "jolt.example.samples.app.Main"
val assetsDir = File("../assets");

tasks.register<JavaExec>("jolt_samples_run_desktop") {
    group = "jolt_examples_desktop"
    description = "Run desktop app"
    mainClass.set(mainClassName)
    classpath = sourceSets["main"].runtimeClasspath
    workingDir = assetsDir

    if(DefaultNativePlatform.getCurrentOperatingSystem().isMacOsX) {
        jvmArgs("-XstartOnFirstThread")
    }
}
import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform

plugins {
    id("java")
}

dependencies {
    implementation(project(":examples:graphics:gdx-wgpu"))
    implementation(project(":examples:samples:core"))

    if(LibExt.useRepoLibs) {
        implementation("com.github.xpenatan.xJolt:jolt-desktop:${LibExt.exampleVersion}")
        implementation("com.github.xpenatan.xJolt:gdx-wgpu:${LibExt.exampleVersion}")
    }
    else {
        implementation(project(":jolt:jolt-desktop"))
        implementation(project(":extensions:gdx:gdx-wgpu"))
    }

    implementation("io.github.monstroussoftware.gdx-webgpu:backend-desktop:${LibExt.gdxWebGPUVersion}")
    implementation("com.badlogicgames.gdx:gdx-platform:${LibExt.gdxVersion}:natives-desktop")
//    implementation("com.github.xpenatan.gdx-imgui:imgui-ext-desktop:${LibExt.gdxImGuiVersion}")
}

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.java11Target)
    targetCompatibility = JavaVersion.toVersion(LibExt.java11Target)
}

val mainClassName = "jolt.example.samples.app.Main"
val assetsDir = File("../assets");

tasks.register<JavaExec>("jolt_samples_run_desktop_wgpu") {
    group = "jolt_examples_desktop"
    description = "Run WebGPU desktop app"
    mainClass.set(mainClassName)
    classpath = sourceSets["main"].runtimeClasspath
    workingDir = assetsDir

    if(DefaultNativePlatform.getCurrentOperatingSystem().isMacOsX) {
        jvmArgs("-XstartOnFirstThread")
    }
}
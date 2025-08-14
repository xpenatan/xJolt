plugins {
    id("java")
    id("org.gretty") version("3.1.0")
}

gretty {
    contextPath = "/"
    extraResourceBase("build/dist/webapp")
}

dependencies {
    implementation(project(":examples:samples:core"))

    implementation("com.badlogicgames.gdx:gdx:${LibExt.gdxVersion}:sources")
    implementation("com.github.xpenatan.gdx-teavm:backend-teavm:${LibExt.gdxTeaVMVersion}:sources")

    if(LibExt.useRepoLibs) {
        implementation("com.github.xpenatan.xJolt:jolt-teavm:-SNAPSHOT")
        implementation("com.github.xpenatan.xJolt:jolt-wgpu:-SNAPSHOT")
    }
    else {
        implementation(project(":jolt:jolt-teavm"))
        implementation(project(":jolt-wgpu"))
    }

    implementation("com.badlogicgames.gdx:gdx:${LibExt.gdxVersion}")
    implementation("com.github.xpenatan.gdx-teavm:backend-teavm:${LibExt.gdxTeaVMVersion}")
    implementation("io.github.monstroussoftware.gdx-webgpu:gdx-teavm-webgpu:${LibExt.gdxWebGPUVersion}")
//    implementation("com.github.xpenatan.gdx-imgui:imgui-ext-teavm:${LibExt.gdxImGuiVersion}")
}

val mainClassName = "jolt.example.samples.app.Build"

tasks.register<JavaExec>("jolt_samples_build_wgpu") {
    group = "jolt_examples_teavm"
    description = "Build SamplesApp WGPU example"
    mainClass.set(mainClassName)
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register("jolt_samples_run_teavm_wgpu") {
    group = "jolt_examples_teavm"
    description = "Run teavm WGPU app"
    val list = listOf("jolt_samples_build_wgpu", "jettyRun")
    dependsOn(list)

    tasks.findByName("jettyRun")?.mustRunAfter("jolt_samples_build_wgpu")
}
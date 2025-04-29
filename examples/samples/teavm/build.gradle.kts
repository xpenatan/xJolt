plugins {
    id("org.gretty") version("3.1.0")
}

gretty {
    contextPath = "/"
    extraResourceBase("build/dist/webapp")
}

dependencies {
    implementation(project(":examples:samples:core"))

    if(LibExt.exampleUseRepoLibs) {
        implementation("com.github.xpenatan.gdx-jolt:jolt-teavm:-SNAPSHOT")
    }
    else {
        implementation(project(":jolt:jolt-teavm"))
    }

    implementation("com.badlogicgames.gdx:gdx:${LibExt.gdxVersion}")
    implementation("com.github.xpenatan.gdx-teavm:backend-teavm:${LibExt.gdxTeaVMVersion}")
    implementation("com.github.xpenatan.gdx-imgui:imgui-ext-teavm:${LibExt.gdxImGuiVersion}")
}

val mainClassName = "jolt.example.samples.app.Build"

tasks.register<JavaExec>("jolt_samples_build") {
    group = "jolt_examples_teavm"
    description = "Build SamplesApp example"
    mainClass.set(mainClassName)
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register("jolt_samples_run_teavm") {
    group = "jolt_examples_teavm"
    description = "Run teavm app"
    val list = listOf("jolt_samples_build", "jettyRun")
    dependsOn(list)

    tasks.findByName("jettyRun")?.mustRunAfter("jolt_samples_build")
}
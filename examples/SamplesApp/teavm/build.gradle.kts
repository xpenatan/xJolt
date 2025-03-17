plugins {
    id("org.gretty") version("3.1.0")
}

gretty {
    contextPath = "/"
    extraResourceBase("build/dist/webapp")
}

dependencies {
    implementation(project(":examples:SamplesApp:core"))

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

tasks.register<JavaExec>("SamplesApp-build") {
    group = "example-teavm"
    description = "Build SamplesApp example"
    mainClass.set(mainClassName)
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register("SamplesApp-run-teavm") {
    group = "example-teavm"
    description = "Run teavm app"
    val list = listOf("SamplesApp-build", "jettyRun")
    dependsOn(list)

    tasks.findByName("jettyRun")?.mustRunAfter("SamplesApp-build")
}
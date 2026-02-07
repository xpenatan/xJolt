plugins {
    id("java")
}

dependencies {
    implementation(project(":examples:graphics:gdx-gl"))
    implementation(project(":examples:samples:core"))

    implementation("com.badlogicgames.gdx:gdx:${LibExt.gdxVersion}:sources")

    if(LibExt.useRepoLibs) {
        implementation("com.github.xpenatan.xJolt:jolt-teavm:${LibExt.exampleVersion}")
        implementation("com.github.xpenatan.xJolt:gdx-gl:${LibExt.exampleVersion}")
    }
    else {
        implementation(project(":jolt:jolt-teavm"))
        implementation(project(":extensions:gdx:gdx-gl"))
    }

    implementation("com.badlogicgames.gdx:gdx:${LibExt.gdxVersion}")
    implementation("com.github.xpenatan.gdx-teavm:backend-web:${LibExt.gdxTeaVMVersion}")
    implementation("com.github.xpenatan.gdx-teavm:backend-web:${LibExt.gdxTeaVMVersion}:sources")
//    implementation("com.github.xpenatan.gdx-imgui:imgui-ext-teavm:${LibExt.gdxImGuiVersion}")
}

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.java11Target)
    targetCompatibility = JavaVersion.toVersion(LibExt.java11Target)
}

val mainClassName = "jolt.example.samples.app.Build"

tasks.register<JavaExec>("jolt_samples_run_teavm") {
    group = "jolt_examples_teavm"
    description = "Build SamplesApp example"
    mainClass.set(mainClassName)
    classpath = sourceSets["main"].runtimeClasspath
}
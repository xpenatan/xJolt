plugins {
    id("java-library")
}

dependencies {
    api(project(":examples:graphics:gdx-shared"))

    implementation("com.badlogicgames.gdx:gdx:${LibExt.gdxVersion}")
    api("io.github.monstroussoftware.gdx-webgpu:gdx-webgpu:${LibExt.gdxWebGPUVersion}")

    api(project(":extensions:gdx:gdx-wgpu"))
}

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.java11Target)
    targetCompatibility = JavaVersion.toVersion(LibExt.java11Target)
}
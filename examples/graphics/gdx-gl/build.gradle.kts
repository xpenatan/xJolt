plugins {
    id("java-library")
}

dependencies {
    api(project(":examples:graphics:gdx-shared"))

    implementation("com.badlogicgames.gdx:gdx:${LibExt.gdxVersion}")
    api("com.github.xpenatan.xImGui:gdx-gl-impl:${LibExt.gdxImGuiVersion}")
    api(project(":extensions:gdx:gdx-gl"))
}

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.java8Target)
    targetCompatibility = JavaVersion.toVersion(LibExt.java8Target)
}
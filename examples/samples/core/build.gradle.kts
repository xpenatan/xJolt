plugins {
    id("java-library")
}

dependencies {
    implementation("com.github.xpenatan.gdx-gltf:core:dev-SNAPSHOT")

    if(LibExt.useRepoLibs) {
        implementation("com.github.xpenatan.xJolt:jolt-core:-SNAPSHOT")
        implementation("com.github.xpenatan.xJolt:gdx-utils:-SNAPSHOT")
    }
    else {
        implementation(project(":jolt:jolt-core"))
        implementation(project(":extensions:gdx:gdx-utils"))
    }

    implementation(project(":examples:graphics:gdx-shared"))
    api("com.badlogicgames.gdx:gdx:${LibExt.gdxVersion}")
//    implementation("com.github.xpenatan.gdx-imgui:gdx-impl:${LibExt.gdxImGuiVersion}")
//    implementation("com.github.xpenatan.gdx-imgui:imgui-ext-core:${LibExt.gdxImGuiVersion}")
}

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.java8Target)
    targetCompatibility = JavaVersion.toVersion(LibExt.java8Target)
}
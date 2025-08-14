plugins {
    id("java-library")
}

dependencies {
    implementation("com.github.xpenatan.gdx-gltf:core:dev-SNAPSHOT")

    if(LibExt.useRepoLibs) {
        implementation("com.github.xpenatan.xJolt:jolt-core:-SNAPSHOT")
        implementation("com.github.xpenatan.xJolt:jolt-gdx:-SNAPSHOT")
    }
    else {
        implementation(project(":jolt:jolt-core"))
        implementation(project(":jolt-gdx"))
    }

    api("com.badlogicgames.gdx:gdx:${LibExt.gdxVersion}")
//    implementation("com.github.xpenatan.gdx-imgui:gdx-impl:${LibExt.gdxImGuiVersion}")
//    implementation("com.github.xpenatan.gdx-imgui:imgui-ext-core:${LibExt.gdxImGuiVersion}")
}

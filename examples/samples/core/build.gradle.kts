dependencies {
    implementation(project(":examples:samples:base"))
    implementation("com.github.xpenatan.gdx-gltf:core:dev-SNAPSHOT")

    if(LibExt.exampleUseRepoLibs) {
        implementation("com.github.xpenatan.gdx-jolt:jolt-core:-SNAPSHOT")
        implementation("com.github.xpenatan.gdx-jolt:jolt-gdx:-SNAPSHOT")
    }
    else {
        implementation(project(":jolt:jolt-core"))
        implementation(project(":jolt-gdx"))
    }

    api("com.badlogicgames.gdx:gdx:${LibExt.gdxVersion}")
//    implementation("com.github.xpenatan.gdx-imgui:gdx-impl:${LibExt.gdxImGuiVersion}")
//    implementation("com.github.xpenatan.gdx-imgui:imgui-ext-core:${LibExt.gdxImGuiVersion}")
}

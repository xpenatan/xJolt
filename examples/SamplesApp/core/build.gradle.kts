dependencies {
    implementation(project(":examples:SamplesApp:base"))

    if(LibExt.exampleUseRepoLibs) {
        implementation("com.github.xpenatan.gdx-jolt:jolt-core:-SNAPSHOT")
        implementation("com.github.xpenatan.gdx-jolt:jolt-gdx:-SNAPSHOT")
    }
    else {
        implementation(project(":jolt:jolt-core"))
        implementation(project(":jolt-gdx"))
    }

    implementation("com.badlogicgames.gdx:gdx:${LibExt.gdxVersion}")
}

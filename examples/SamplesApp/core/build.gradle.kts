dependencies {
    implementation(project(":examples:SamplesApp:base"))

    if(LibExt.exampleUseRepoLibs) {
        implementation("com.github.xpenatan.gdx-jolt:jolt-core:-SNAPSHOT")
    }
    else {
        implementation(project(":jolt:jolt-core"))
    }

    implementation("com.badlogicgames.gdx:gdx:${LibExt.gdxVersion}")
}

plugins {
    id("java-library")
}

dependencies {
    api(project(":examples:graphics:gdx-shared"))

    implementation("com.badlogicgames.gdx:gdx:${LibExt.gdxVersion}")

    if(LibExt.useRepoLibs) {
        api("com.github.xpenatan.xJolt:gdx-gl:-SNAPSHOT")
    }
    else {
        api(project(":extensions:gdx:gdx-gl"))
    }
}

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.java8Target)
    targetCompatibility = JavaVersion.toVersion(LibExt.java8Target)
}
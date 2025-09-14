plugins {
    id("java-library")
}

dependencies {
    implementation("com.badlogicgames.gdx:gdx:${LibExt.gdxVersion}")

    if(LibExt.useRepoLibs) {
        api("com.github.xpenatan.xJolt:jolt-core:-SNAPSHOT")
        api("com.github.xpenatan.xJolt:gdx-utils:-SNAPSHOT")
    }
    else {
        api(project(":jolt:jolt-core"))
        implementation(project(":extensions:gdx:gdx-utils"))
    }
}

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.java8Target)
    targetCompatibility = JavaVersion.toVersion(LibExt.java8Target)
}
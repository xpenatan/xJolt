plugins {
    id("java")
}

val moduleName = "jolt-teavm"

val emscriptenFile = "$projectDir/../jolt-build/build/c++/libs/emscripten/jolt.wasm.js"

tasks.jar {
    from(emscriptenFile)
}

dependencies {
    implementation("com.badlogicgames.gdx:gdx:${LibExt.gdxVersion}")
    implementation("com.github.xpenatan.jParser:loader-core:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:loader-teavm:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:idl-core:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:idl-teavm:${LibExt.jParserVersion}")
}

tasks.named("clean") {
    doFirst {
        val srcPath = "$projectDir/src/main/java"
        val jsPath = "$projectDir/src/main/resources/jolt.wasm.js"
        project.delete(files(srcPath, jsPath))
    }
}

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.java11Target)
    targetCompatibility = JavaVersion.toVersion(LibExt.java11Target)
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = moduleName
            group = LibExt.groupId
            version = LibExt.libVersion
            from(components["java"])
        }
    }
}
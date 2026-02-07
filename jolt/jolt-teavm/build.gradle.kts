plugins {
    id("java")
}

val moduleName = "jolt-teavm"

val emscriptenJS = "$projectDir/../jolt-build/build/c++/libs/emscripten/jolt.js"
val emscriptenWASM = "$projectDir/../jolt-build/build/c++/libs/emscripten/jolt.wasm"

tasks.jar {
    from(emscriptenJS, emscriptenWASM)
}

dependencies {
    implementation("com.github.xpenatan.jParser:loader-core:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:loader-teavm:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:idl-core:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:idl-teavm:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:idl-helper-core:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:idl-helper-teavm:${LibExt.jParserVersion}")
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
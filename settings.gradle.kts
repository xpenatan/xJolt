// Core
include(":jolt:jolt-build")
include(":jolt:jolt-base")
include(":jolt:jolt-core")
include(":jolt:jolt-desktop")
include(":jolt:jolt-teavm")
include(":jolt:jolt-android")
include(":jolt-gdx")
include(":jolt-wgpu")

// Examples
include(":examples:samples:base")
include(":examples:samples:core")
include(":examples:samples:desktop")
include(":examples:samples:desktop-wgpu")
include(":examples:samples:teavm")
include(":examples:samples:teavm-wgpu")
include(":examples:samples:android")

includeBuild("E:\\Dev\\Projects\\java\\gdx-webgpu") {
    dependencySubstitution {
        substitute(module("io.github.monstroussoftware.gdx-webgpu:gdx-webgpu")).using(project(":gdx-webgpu"))
        substitute(module("io.github.monstroussoftware.gdx-webgpu:gdx-desktop-webgpu")).using(project(":backends:gdx-desktop-webgpu"))
        substitute(module("io.github.monstroussoftware.gdx-webgpu:gdx-teavm-webgpu")).using(project(":backends:gdx-teavm-webgpu"))
    }
}

//includeBuild("E:\\Dev\\Projects\\java\\gdx-teavm") {
//    dependencySubstitution {
//        substitute(module("com.github.xpenatan.gdx-teavm:backend-teavm")).using(project(":backends:backend-teavm"))
//    }
//}
//
//includeBuild("E:\\Dev\\Projects\\java\\jParser") {
//    dependencySubstitution {
//        substitute(module("com.github.xpenatan.jParser:jParser-base")).using(project(":jParser:base"))
//        substitute(module("com.github.xpenatan.jParser:jParser-build")).using(project(":jParser:builder"))
//        substitute(module("com.github.xpenatan.jParser:jParser-build-tool")).using(project(":jParser:builder-tool"))
//        substitute(module("com.github.xpenatan.jParser:jParser-core")).using(project(":jParser:core"))
//        substitute(module("com.github.xpenatan.jParser:jParser-cpp")).using(project(":jParser:cpp"))
//        substitute(module("com.github.xpenatan.jParser:jParser-idl")).using(project(":jParser:idl"))
//        substitute(module("com.github.xpenatan.jParser:jParser-teavm")).using(project(":jParser:teavm"))
//        substitute(module("com.github.xpenatan.jParser:loader-core")).using(project(":jParser:loader:loader-core"))
//        substitute(module("com.github.xpenatan.jParser:loader-teavm")).using(project(":jParser:loader:loader-teavm"))
//    }
//}
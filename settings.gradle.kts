// Core
include(":jolt:jolt-build")
include(":jolt:jolt-base")
include(":jolt:jolt-core")
include(":jolt:jolt-desktop")
include(":jolt:jolt-teavm")
include(":jolt:jolt-android")

// Extension
include(":extensions:gdx:gdx-utils")
include(":extensions:gdx:gdx-gl")
include(":extensions:gdx:gdx-wgpu")

// Examples
include(":examples:graphics:gdx-shared")
include(":examples:graphics:gdx-gl")
include(":examples:graphics:gdx-wgpu")
include(":examples:samples:core")
include(":examples:samples:desktop")
include(":examples:samples:desktop-wgpu")
include(":examples:samples:teavm")
include(":examples:samples:teavm-wgpu")
include(":examples:samples:android")

//includeBuild("E:\\Dev\\Projects\\java\\gdx-webgpu") {
//    dependencySubstitution {
//        substitute(module("io.github.monstroussoftware.gdx-webgpu:gdx-webgpu")).using(project(":gdx-webgpu"))
//        substitute(module("io.github.monstroussoftware.gdx-webgpu:gdx-desktop-webgpu")).using(project(":backends:gdx-desktop-webgpu"))
//        substitute(module("io.github.monstroussoftware.gdx-webgpu:gdx-teavm-webgpu")).using(project(":backends:gdx-teavm-webgpu"))
//    }
//}

//includeBuild("E:\\Dev\\Projects\\java\\gdx-teavm") {
//    dependencySubstitution {
//        substitute(module("com.github.xpenatan.gdx-teavm:backend-teavm")).using(project(":backends:backend-teavm"))
//    }
//}
//
//includeBuild("E:\\Dev\\Projects\\java\\jParser") {
//    dependencySubstitution {
//        substitute(module("com.github.xpenatan.jParser:jParser-base")).using(project(":jParser:jParser-base"))
//        substitute(module("com.github.xpenatan.jParser:jParser-build")).using(project(":jParser:jParser-build"))
//        substitute(module("com.github.xpenatan.jParser:jParser-build-tool")).using(project(":jParser:jParser-build-tool"))
//        substitute(module("com.github.xpenatan.jParser:jParser-core")).using(project(":jParser:jParser-core"))
//        substitute(module("com.github.xpenatan.jParser:jParser-cpp")).using(project(":jParser:jParser-cpp"))
//        substitute(module("com.github.xpenatan.jParser:jParser-idl")).using(project(":jParser:jParser-idl"))
//        substitute(module("com.github.xpenatan.jParser:jParser-teavm")).using(project(":jParser:jParser-teavm"))
//        substitute(module("com.github.xpenatan.jParser:idl-core")).using(project(":idl:idl-core"))
//        substitute(module("com.github.xpenatan.jParser:idl-teavm")).using(project(":idl:idl-teavm"))
//        substitute(module("com.github.xpenatan.jParser:loader-core")).using(project(":loader:loader-core"))
//        substitute(module("com.github.xpenatan.jParser:loader-teavm")).using(project(":loader:loader-teavm"))
//    }
//}
plugins {
    id("java")
}

val moduleName = "jolt-desktop"

val libDir = "${rootProject.projectDir}/jolt"
//val windowsFile = "$libDir/jolt-build/build/c++/libs/windows/jolt64.dll"
val windowsFile = "$libDir/jolt-build/build/c++/libs/windows/vc/jolt64.dll"
val linuxFile = "$libDir/jolt-build/build/c++/libs/linux/libjolt64.so"
val macArmFile = "$libDir/jolt-build/build/c++/libs/mac/arm/libjoltarm64.dylib"
val macFile = "$libDir/jolt-build/build/c++/libs/mac/libjolt64.dylib"

tasks.jar {
    from(windowsFile)
    from(linuxFile)
    from(macArmFile)
    from(macFile)
}

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.java8Target)
    targetCompatibility = JavaVersion.toVersion(LibExt.java8Target)
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = moduleName
            from(components["java"])
        }
    }
}
plugins {
    id("java-library")
}

val moduleName = "jolt-core"

dependencies {
    api("com.github.xpenatan.jParser:loader-core:${LibExt.jParserVersion}")

    testImplementation(project(":jolt:jolt-desktop"))
    testImplementation("junit:junit:${LibExt.jUnitVersion}")
}

tasks.named("clean") {
    doFirst {
        val srcPath = "$projectDir/src/main/java"
        project.delete(files(srcPath))
    }
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
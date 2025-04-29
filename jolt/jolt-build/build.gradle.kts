import de.undercouch.gradle.tasks.download.Download
import org.gradle.kotlin.dsl.support.unzipTo

plugins {
    id("de.undercouch.download") version("5.4.0")
}

val mainClassName = "Build"

dependencies {
    implementation(project(":jolt:jolt-base"))
    implementation("com.github.xpenatan.jParser:jParser-core:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:jParser-build:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:jParser-build-tool:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:jParser-teavm:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:jParser-cpp:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:jParser-idl:${LibExt.jParserVersion}")
}

val buildDir = layout.buildDirectory.get().asFile
val zippedPath = "${buildDir}/jolt-source.zip"
val sourcePath = "${buildDir}/jolt-source"
val sourceDestination = "${buildDir}/jolt/"

tasks.register<Download>("jolt_download_source") {
    group = "jolt"
    description = "Download jolt source"
    src("https://github.com/jrouwe/JoltPhysics/archive/refs/tags/v5.3.0.zip")
    dest(File(zippedPath))
    doLast {
        unzipTo(File(sourcePath), dest)
        copy{
            from(sourcePath)
            into(sourceDestination)

            eachFile {
                val paths = relativePath.segments.drop(1)
                relativePath = RelativePath(true, *paths.toTypedArray())
            }
        }
        delete(sourcePath)
        delete(zippedPath)
    }
}

tasks.register<JavaExec>("jolt_build_project") {
    group = "jolt"
    description = "Generate native project"
    mainClass.set(mainClassName)
    args = mutableListOf()
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("jolt_build_project_all") {
    group = "jolt"
    description = "Generate native project"
    mainClass.set(mainClassName)
    args = mutableListOf("teavm", "windows64", "linux64", "mac64", "macArm", "android", "ios")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("jolt_build_project_teavm") {
    group = "jolt"
    description = "Generate native project"
    mainClass.set(mainClassName)
    args = mutableListOf("teavm")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("jolt_build_project_windows64") {
    group = "jolt"
    description = "Generate native project"
    mainClass.set(mainClassName)
    args = mutableListOf("windows64")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("jolt_build_project_linux64") {
    group = "jolt"
    description = "Generate native project"
    mainClass.set(mainClassName)
    args = mutableListOf("linux64")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("jolt_build_project_mac64") {
    group = "jolt"
    description = "Generate native project"
    mainClass.set(mainClassName)
    args = mutableListOf("mac64")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("jolt_build_project_macArm") {
    group = "jolt"
    description = "Generate native project"
    mainClass.set(mainClassName)
    args = mutableListOf("macArm")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("jolt_build_project_android") {
    group = "jolt"
    description = "Generate native project"
    mainClass.set(mainClassName)
    args = mutableListOf("android")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("jolt_build_project_ios") {
    group = "jolt"
    description = "Generate native project"
    mainClass.set(mainClassName)
    args = mutableListOf("ios")
    classpath = sourceSets["main"].runtimeClasspath
}
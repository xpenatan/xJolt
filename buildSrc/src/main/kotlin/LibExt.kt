import java.io.File
import java.util.Properties

object LibExt {
    const val groupId = "com.github.xpenatan.gdx-jolt"
    val libVersion: String = getVersion()

    //Library dependencies
    const val gdxVersion = "1.13.5"
    const val jParserVersion = "-SNAPSHOT"
    const val teaVMVersion = "0.12.1"

    //Example dependencies
    const val gdxTeaVMVersion = "-SNAPSHOT"
    const val gdxImGuiVersion = "-SNAPSHOT"
    const val jUnitVersion = "4.12"

    const val exampleUseRepoLibs = true
}

private fun getVersion(): String {
    val isReleaseStr = System.getenv("RELEASE")
    val isRelease = isReleaseStr != null && isReleaseStr.toBoolean()
    var libVersion = "-SNAPSHOT"
    val file = File("gradle.properties")
    if(file.exists()) {
        val properties = Properties()
        properties.load(file.inputStream())
        val version = properties.getProperty("version")
        if(isRelease) {
            libVersion = version
        }
    }
    else {
        if(isRelease) {
            throw RuntimeException("properties should exist")
        }
    }
    println("Lib Version: $libVersion")
    return libVersion
}

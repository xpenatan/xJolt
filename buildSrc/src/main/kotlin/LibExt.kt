import java.io.File
import java.util.Properties

object LibExt {
    const val groupId = "com.github.xpenatan.xJolt"
    const val libName = "xJolt"
    var isRelease = false
    var libVersion: String = ""
        get() {
            return getVersion()
        }

    //Library dependencies
    const val gdxVersion = "1.13.5"
    const val jParserVersion = "1.0.0-b14"
    const val teaVMVersion = "0.12.3"
    const val gdxWebGPUVersion = "-SNAPSHOT"

    //Example dependencies
    const val gdxTeaVMVersion = "-SNAPSHOT"
    const val gdxImGuiVersion = "-SNAPSHOT"
    const val jUnitVersion = "4.12"

    const val useRepoLibs = true
}

private fun getVersion(): String {
    var libVersion = "-SNAPSHOT"
    val file = File("gradle.properties")
    if(file.exists()) {
        val properties = Properties()
        properties.load(file.inputStream())
        val version = properties.getProperty("version")
        if(LibExt.isRelease) {
            libVersion = version
        }
    }
    else {
        if(LibExt.isRelease) {
            throw RuntimeException("properties should exist")
        }
    }
    return libVersion
}

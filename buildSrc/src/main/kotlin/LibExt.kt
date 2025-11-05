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

    const val java8Target = "1.8"
    const val java11Target = "11"

    //Library dependencies
    const val gdxVersion = "1.14.0"
    const val jParserVersion = "1.0.0-b26"
    const val gdxWebGPUVersion = "0.7"

    //Example dependencies
    const val gdxTeaVMVersion = "1.4.0"
    const val gdxImGuiVersion = "-SNAPSHOT"
    const val jUnitVersion = "4.12"

    const val useRepoLibs = true
    const val exampleVersion = "-SNAPSHOT"
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

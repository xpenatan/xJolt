plugins {
    id("java")
    id("maven-publish")
    id("signing")
    id("org.jetbrains.kotlin.android") version "1.8.21" apply false
}

buildscript {
    repositories {
        mavenCentral()
        google()
    }

    val kotlinVersion = "2.1.10"

    dependencies {
        classpath("com.android.tools.build:gradle:7.3.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}

allprojects  {

    repositories {
        mavenLocal()
        google()
        mavenCentral()
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/releases/") }
    }

    configurations.configureEach {
        // Check for updates every sync
        resolutionStrategy.cacheChangingModulesFor(0, "seconds")
    }
}

configure(allprojects - project(":jolt:jolt-android") - project(":examples:SamplesApp:android")) {
    apply {
        plugin("java")
    }
    java.sourceCompatibility = JavaVersion.VERSION_11
    java.targetCompatibility = JavaVersion.VERSION_11
}

var libProjects = mutableSetOf(
    project(":jolt:jolt-core"),
    project(":jolt:jolt-desktop"),
    project(":jolt:jolt-teavm"),
    project(":jolt:jolt-android")
)

configure(libProjects) {
    apply(plugin = "maven-publish")
    group = LibExt.groupId
    version = LibExt.libVersion
}

configure(libProjects) {
    apply(plugin = "signing")
    apply(plugin = "maven-publish")

    publishing {
        repositories {
            maven {
                url = uri {
                    val ver = project.version.toString()
                    val isSnapshot = ver.uppercase().contains("SNAPSHOT")
                    val repoUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
                    val repoUrlSnapshot = "https://oss.sonatype.org/content/repositories/snapshots/"
                    if (isSnapshot) repoUrlSnapshot else repoUrl
                }
                credentials {
                    username = System.getenv("USER")
                    password = System.getenv("PASSWORD")
                }
            }
        }
    }

    tasks.withType<Javadoc> {
        options.encoding = "UTF-8"
        (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
    }

    publishing.publications.configureEach {
        if (this is MavenPublication) {
            pom {
                name.set("gdx-jolt")
                description.set("Java JNI based binding for jolt physics")
                url.set("https://github.com/xpenatan/gdx-jolt")
                developers {
                    developer {
                        id.set("Xpe")
                        name.set("Natan")
                    }
                }
                scm {
                    connection.set("scm:git:git://https://github.com/xpenatan/gdx-jolt.git")
                    developerConnection.set("scm:git:ssh://https://github.com/xpenatan/gdx-jolt.git")
                    url.set("http://https://github.com/xpenatan/gdx-jolt/tree/master")
                }
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
            }
        }
    }

    val signingKey = System.getenv("SIGNING_KEY")
    val signingPassword = System.getenv("SIGNING_PASSWORD")
    if (signingKey != null && signingPassword != null) {
        signing {
            useInMemoryPgpKeys(signingKey, signingPassword)
            publishing.publications.configureEach {
                sign(this)
            }
        }
    }
}

tasks.register("download_all_sources") {
    group = "jolt"
    description = "Download all sources"

    val source1 = ":jolt:jolt-build:download_source"

    val list = listOf(source1)
    dependsOn(list)
}
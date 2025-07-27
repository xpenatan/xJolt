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
        maven { url = uri("https://jitpack.io") }
    }

    val kotlinVersion = "2.1.10"

    dependencies {
        classpath("com.android.tools.build:gradle:8.0.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}

allprojects  {

    repositories {
        mavenLocal()
        google()
        mavenCentral()
        maven { url = uri("https://central.sonatype.com/repository/maven-snapshots/") }
        maven { url = uri("https://jitpack.io") }
        maven {
            url = uri("http://teavm.org/maven/repository/")
            isAllowInsecureProtocol = true
        }
    }

    configurations.configureEach {
        // Check for updates every sync
        resolutionStrategy.cacheChangingModulesFor(0, "seconds")
    }
}

configure(allprojects - project(":jolt:jolt-android") - project(":examples:samples:android")) {
    apply {
        plugin("java")
        plugin("java-library")
    }
    java.sourceCompatibility = JavaVersion.VERSION_11
    java.targetCompatibility = JavaVersion.VERSION_11
}

var libProjects = mutableSetOf(
    project(":jolt-gdx"),
    project(":jolt-wgpu"),
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
                url = if (project.version.toString().endsWith("-SNAPSHOT")) {
                    uri("https://central.sonatype.com/repository/maven-snapshots/")
                } else {
                    uri("https://ossrh-staging-api.central.sonatype.com/service/local/staging/deploy/maven2/")
                }
                credentials {
                    username = System.getenv("CENTRAL_PORTAL_USERNAME")
                    password = System.getenv("CENTRAL_PORTAL_PASSWORD")
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
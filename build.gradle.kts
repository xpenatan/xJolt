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
        classpath("com.android.tools.build:gradle:8.5.1")
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

apply(from = "publish.gradle.kts")
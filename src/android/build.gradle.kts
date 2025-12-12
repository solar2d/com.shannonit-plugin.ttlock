// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {

    // Versions
    val agpVersion = "8.8.0" // only one value can be used
    // val agpVersionOld = "7.2.2" // if you need reference

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        // classpath("com.android.tools:r8:8.0.40")
        classpath("com.android.tools.build:gradle:$agpVersion")
        classpath("com.github.dcendents:android-maven-gradle-plugin:1.5")
        // classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.7.3")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}

// Top-level clean task
tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}

// Extra properties (like ext)
val minSdkVersion: Int by extra(18)
val targetSdkVersion: Int by extra(34)
val compileSdkVersion: Int by extra(34)
val buildToolsVersion: String by extra("34.0.0")
val supportLibraryVersion: String by extra("28.0.0")
val lifecycleVersion: String by extra("1.1.1")

allprojects {
    repositories {
        google()

        mavenCentral()

        // Custom flatDir repository
        val nativeDir = if (System.getProperty("os.name").lowercase().contains("windows")) {
            System.getenv("CORONA_ROOT")
        } else {
            "${System.getenv("HOME")}/Library/Application Support/Corona/Native/"
        }

        flatDir {
            dirs(
                "$nativeDir/Corona/android/lib/gradle",
                "$nativeDir/Corona/android/lib/Corona/libs"
            )
        }
    }
}

plugins {
    id("com.android.library")
}

android {
    namespace = "plugin.ttlock"
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        targetSdk = 33
    }
    packagingOptions {
        exclude("com/naef/jnlua/**")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
        debug {
            isMinifyEnabled = false
        }
    }

    // Rename the output AAR to plugin.ttlock.aar
    libraryVariants.all {
        val variantNameCapitalized = name.substring(0, 1).toUpperCase() + name.substring(1)
        val bundleTaskName = "bundle${variantNameCapitalized}Aar"
        tasks.findByName(bundleTaskName)?.doLast {
            val outputDir = File(buildDir, "outputs/aar")
            val original = File(outputDir, "app-release.aar")
            val renamed = File(outputDir, "plugin.ttlock.aar")
            if (original.exists()) {
                original.renameTo(renamed)
                println("Renamed AAR to plugin.ttlock.aar")
            }
        }
    }
}

repositories {
    google()
    mavenCentral()
    flatDir {
        dirs("libs")
    }
}

dependencies {
    // Include TTLock SDK
    implementation(files("libs/ttlock.jar")) // replace with .aar if needed

    // Include any other JARs in libs folder
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // LuaJ / Corona classes for compilation only (won't be packaged)
    compileOnly(files("libs/jnlua.jar"))
}

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

    buildTypes {
        release {
            isMinifyEnabled = false
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
    // All JARs in libs folder
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // Your local AAR
    implementation(files("libs/ttlock.jar"))

    implementation(files("libs/jnlua.jar"))
}

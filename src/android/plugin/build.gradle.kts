import org.gradle.api.tasks.bundling.Tar
import org.gradle.api.tasks.bundling.Compression
import org.gradle.jvm.tasks.Jar

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
            isMinifyEnabled = true
        }
        debug {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(files("libs/ttlock.jar")) // TTLock SDK
    compileOnly(files("libs/jnlua.jar"))     // LuaJ/Corona stub
}

// Task to compile plugin Java sources into a jar
val compilePluginJar by tasks.registering(Jar::class) {
    group = "Solar2Dev"
    archiveBaseName.set("plugin.ttlock")
    from(android.sourceSets["main"].java.srcDirs)
}

// Task to extract compiled classes into a temp folder
val extractPluginClasses by tasks.registering {
    group = "Solar2Dev"
    dependsOn("compileReleaseJavaWithJavac")

    doLast {
        val buildDir = layout.buildDirectory.asFile.get()
        val classesDir = buildDir.resolve("intermediates/javac/release/classes")
        val tmpDir = buildDir.resolve("tmpClasses")

        tmpDir.mkdirs()

        if (!classesDir.exists()) {
            println("WARNING: Compiled classes directory does not exist: $classesDir")
        } else {
            copy {
                from(classesDir)
                into(tmpDir)
            }
            println("Copied compiled classes to $tmpDir")
        }
    }
}

// Task to create data.tgz for Solar2D
val deployToLocalSolar2DRepo by tasks.registering(Tar::class) {
    group = "Solar2Dev"
    dependsOn(extractPluginClasses)

    compression = Compression.GZIP
    archiveFileName.set("data.tgz")

    val rootS2DP = System.getenv("APPDATA") ?: System.getenv("HOME")
    val targetDir = file(
        "$rootS2DP/Solar2DPlugins/Caches/Solar2Directory/solar2d/com.shannonit/plugin.ttlock/android"
    )

    destinationDirectory.set(targetDir)

    into("/") {
        val tmpDir = layout.buildDirectory.asFile.get().resolve("tmpClasses")
        from(tmpDir)
        includeEmptyDirs = false
    }

    doLast {
        println("\n== Plugin Installed ==")
        println("Saved to: $targetDir")
        println("Remember: this overrides any existing Solar2D plugin with the same name")
    }
}


// Rename AAR after build
android.libraryVariants.all {
    val variantNameCapitalized = name.capitalize() // Use capitalize() instead of replaceFirstChar
    val bundleTaskName = "bundle${variantNameCapitalized}Aar"
    tasks.findByName(bundleTaskName)?.doLast {
        val outputDir = File(buildDir, "outputs/aar")
        val original = File(outputDir, "plugin-release.aar")
        val renamed = File(outputDir, "plugin.ttlock.aar")
        if (original.exists()) {
            original.renameTo(renamed)
            println("Renamed AAR to plugin.ttlock.aar")
        }
    }
}

// Finalize tasks
afterEvaluate {
    tasks.named("bundleReleaseAar") {
        finalizedBy(extractPluginClasses, deployToLocalSolar2DRepo)
    }
}

Sources for the plugin plugin.shannonit.

Add following to your build.settings to use:

{
    plugins = {
        "plugin.ttlock" = {
            publisherId = "com.shannonit",
        },
    },
}


https://repo1.maven.org/maven2/com/ttlock/ttlock/3.1.9/

inside the app folder i.e. app/
gradle wrapper --gradle-version 9.2.1
gradle init
.\gradlew assembleRelease
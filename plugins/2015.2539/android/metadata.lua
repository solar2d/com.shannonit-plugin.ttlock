-- metadata.lua
return {
    plugin = {
        format = "aar",        -- or "aar" if using an Android library
        publisherId = "com.shannonit",
        name = "plugin.ttlock",
        version = "1.0.0",
        minSdkVersion = 23,    -- minimum Android SDK
        maxSdkVersion = 34,    -- optional
        supportedPlatforms = {
            android = true,
            iphone = false      -- set true if you have iOS implementation
        },
        description = "TTLock Bluetooth lock integration for Solar2D",
        license = "MIT",       -- optional
    }
}

local metadata =
{
    plugin =
    {
        format = "aar",
        manifest =
        {
            usesPermissions =
            {
                -- Existing permissions
                "android.permission.INTERNET",
                "android.permission.ACCESS_NETWORK_STATE",
                "android.permission.READ_PHONE_STATE",

                -- Bluetooth / BLE permissions
                "android.permission.BLUETOOTH",
                "android.permission.BLUETOOTH_ADMIN",
                "android.permission.BLUETOOTH_SCAN",
                "android.permission.BLUETOOTH_CONNECT",
            },

            usesFeatures =
            {
            },

            applicationChildElements =
            {
                -- Optional extra manifest entries
            },
        },
    },

    coronaManifest =
    {
        dependencies =
        {
            -- ["shared.memoryBitmap"] = "com.coronalabs",
        },
    },
}

return metadata

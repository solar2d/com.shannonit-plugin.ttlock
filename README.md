## Sources for the plugin `plugin.shannonit`

Add the following to your `build.settings` to use it:

```lua
{
    plugins = {
        ["plugin.ttlock"] = {
            publisherId = "com.shannonit",
        },
    },
}
```

Known Issues

BLE permission issues  
Bluetooth Low Energy (BLE) uses dangerous permissions on Android, which are currently not requested at runtime.

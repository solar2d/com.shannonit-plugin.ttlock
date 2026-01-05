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

BLE issues
- The error says a one of  No class defined found error; com.ttlock.bl.wek.qpi.BluetoothIml$d while if you look inside the .aar produced the class mentioned is found

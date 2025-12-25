local ttlock = require("plugin.ttlock")

-- Debug overlay text
local debugText = display.newText({
    text = "TTLock Debug:\n",
    x = display.contentCenterX,
    y = 100,
    width = display.contentWidth - 40,  -- keep inside screen
    height = display.contentHeight - 200, -- fit vertically
    font = native.systemFont,
    fontSize = 16,
    align = "left"
})
debugText:setFillColor(1, 1, 1)
debugText.anchorY = 0

-- Function to log debug messages
local function logDebug(msg)
    debugText.text = debugText.text .. "\n" .. msg
    print(msg)  -- also log to console
end

-- TTLock event listener
local function ttlockListener(event)
    logDebug("TTLock Event received")
    for k,v in pairs(event) do
        logDebug(k .. ": " .. tostring(v))
    end

    if event.type == "found" then
        logDebug("Device found: " .. event.name .. " (" .. event.mac .. ")")
    end
end

ttlock.init(ttlockListener)

-- BLE initialization function with retry
local function startBLE()
    local isOn = ttlock.isBLEEnabled()
    logDebug("Bluetooth enabled: " .. tostring(isOn))

    if not isOn then
        logDebug("Requesting Bluetooth enable...")
        ttlock.requestBleEnable()
        -- Retry after 2 seconds
        timer.performWithDelay(2000, startBLE)
        return
    end

    logDebug("Starting BLE service...")
    local success, err = pcall(function()
        ttlock.startBleService()
    end)

    if not success then
        logDebug("Error starting BLE service: " .. tostring(err))
        -- Retry after 3 seconds
        timer.performWithDelay(3000, startBLE)
        return
    end

    -- Start scanning after 2 seconds
    timer.performWithDelay(2000, function()
        logDebug("Starting TTLock scan...")
        ttlock.startBTDeviceScan()
    end)

    -- Stop scan after 10 seconds
    timer.performWithDelay(12000, function()
        logDebug("Stopping scan...")
        ttlock.stopBTDeviceScan()
    end)

    -- Initialize lock after 14 seconds
    timer.performWithDelay(14000, function()
        logDebug("Initializing lock...")
        ttlock.lockInitialize()
    end)
end

-- Request runtime permissions first (Android 12+)
if ttlock.requestPermissions then
    logDebug("Requesting runtime permissions...")
    ttlock.requestPermissions()
    -- Delay BLE start to give user time to grant permissions
    timer.performWithDelay(2000, startBLE)
else
    -- No permissions wrapper, just start BLE (older Android)
    startBLE()
end

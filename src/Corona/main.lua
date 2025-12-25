local ttlock = require("plugin.ttlock")

-- Debug overlay
local debugText = display.newText({
    text = "TTLock Debug:\n",
    x = display.contentCenterX,
    y = 100,
    width = display.contentWidth - 40,
    height = display.contentHeight - 200,
    font = native.systemFont,
    fontSize = 16,
    align = "left"
})
debugText:setFillColor(1, 1, 1)
debugText.anchorY = 0

-- Function to log debug messages
local function logDebug(msg)
    debugText.text = debugText.text .. "\n" .. msg
    print(msg)
end

-- TTLock event listener
local function ttlockListener(event)
    logDebug("TTLock Event received")
    for k,v in pairs(event) do
        logDebug(k .. ": " .. tostring(v))
    end

    if event.type == "found" then
        logDebug("Device found: " .. event.name .. " (" .. event.mac .. ")")
    elseif event.type == "lock_initialized" then
        logDebug("Lock initialized successfully")
    elseif event.type then
        logDebug("Event: " .. tostring(event.type))
    end
end

-- Initialize TTLock plugin
ttlock.init(ttlockListener)

-- BLE initialization with permissions check (Android 12+)
local function startBLE()
    local isOn = ttlock.isBLEEnabled()
    logDebug("Bluetooth enabled: " .. tostring(isOn))

    if not isOn then
        logDebug("Requesting Bluetooth enable...")
        ttlock.requestBleEnable()
        timer.performWithDelay(2000, startBLE)
        return
    end

    logDebug("Starting BLE service...")
    local success, err = pcall(function()
        ttlock.startBleService()
    end)
    if not success then
        logDebug("Error starting BLE service: " .. tostring(err))
        timer.performWithDelay(3000, startBLE)
        return
    end

    -- Start scanning after service started
    timer.performWithDelay(1000, function()
        logDebug("Starting TTLock scan...")
        ttlock.startBTDeviceScan()
    end)

    -- Stop scanning after 10 seconds
    timer.performWithDelay(11000, function()
        logDebug("Stopping scan...")
        ttlock.stopBTDeviceScan()
    end)

    -- Initialize lock after scanning
    timer.performWithDelay(13000, function()
        logDebug("Initializing lock...")
        ttlock.lockInitialize()
    end)
end

-- Request runtime permissions first
local function requestPermissionsAndStart()
    if ttlock.requestPermissions then
        logDebug("Requesting runtime permissions...")
        ttlock.requestPermissions()
        -- Give user 2 seconds to grant permissions
        timer.performWithDelay(2000, startBLE)
    else
        startBLE()
    end
end

-- Start BLE flow
requestPermissionsAndStart()

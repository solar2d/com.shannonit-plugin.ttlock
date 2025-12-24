local ttlock = require("plugin.ttlock")

local debugText = display.newText({
    text = "TTLock Debug:\n",
    x = display.contentCenterX,
    y = 100,
    width = display.contentWidth - 20,
    height = 300,
    font = native.systemFont,
    fontSize = 16,
    align = "left"
})
debugText:setFillColor(1,1,1)

local function logDebug(msg)
    debugText.text = debugText.text .. "\n" .. msg
    print(msg)  -- also log to console
end

local function ttlockListener(event)
    logDebug("Event received")
    for k,v in pairs(event) do
        logDebug(k .. ": " .. tostring(v))
    end

    if event.type == "found" then
        logDebug("Device found: " .. event.name .. " (" .. event.mac .. ")")
    end
end

ttlock.init(ttlockListener)

-- Check Bluetooth
local isOn = ttlock.isBLEEnabled()
logDebug("Bluetooth enabled: " .. tostring(isOn))

if not isOn then
    logDebug("Requesting Bluetooth enable...")
    ttlock.requestBleEnable()
end

ttlock.startBleService()

timer.performWithDelay(2000, function()
    logDebug("Starting scan...")
    ttlock.startBTDeviceScan()
end)

timer.performWithDelay(10000, function()
    logDebug("Stopping scan...")
    ttlock.stopBTDeviceScan()
end)

timer.performWithDelay(12000, function()
    logDebug("Initializing lock...")
    ttlock.lockInitialize()
end)

--------------------------------------------------------------------------------
-- main.lua
-- TTLock Solar2D test app (auto-scroll safe)
--------------------------------------------------------------------------------

local widget = require("widget")
local ttlock = require("plugin.ttlock")

--------------------------------------------------------------------------------
-- UI SETUP
--------------------------------------------------------------------------------

display.setStatusBar(display.HiddenStatusBar)

local group = display.newGroup()

local title = display.newText({
    parent = group,
    text = "TTLock Scanner",
    x = display.contentCenterX,
    y = 40,
    font = native.systemFontBold,
    fontSize = 24
})

--------------------------------------------------------------------------------
-- LOG VIEW (AUTO SCROLL)
--------------------------------------------------------------------------------

local LOG_TOP = 80
local LOG_BOTTOM_MARGIN = 260

local logText = display.newText({
    parent = group,
    text = "",
    x = 20,
    y = LOG_TOP,
    width = display.contentWidth - 40,
    height = display.contentHeight - LOG_BOTTOM_MARGIN,
    font = native.systemFont,
    fontSize = 14,
    align = "left"
})

logText.anchorX = 0
logText.anchorY = 0

local function log(msg)
    print(msg)
    logText.text = logText.text .. msg .. "\n"

    -- Auto scroll when overflowing
    local bounds = logText.contentBounds
    if bounds then
        local visibleBottom = display.contentHeight - LOG_BOTTOM_MARGIN + LOG_TOP
        local overflow = bounds.yMax - visibleBottom
        if overflow > 0 then
            logText.y = logText.y - overflow
        end
    end
end

--------------------------------------------------------------------------------
-- PERMISSION STATUS (READ ONLY)
--------------------------------------------------------------------------------

local REQUIRED_PERMISSIONS = {
    "android.permission.ACCESS_FINE_LOCATION",
    "android.permission.BLUETOOTH_SCAN",
    "android.permission.BLUETOOTH_CONNECT"
}

local function printPermissionStatus()
    log("Permission status:")
    for _, perm in ipairs(REQUIRED_PERMISSIONS) do
        local granted = system.getInfo("androidAppPermission", perm)
        log("  " .. perm .. " = " .. (granted and "GRANTED" or "MISSING"))
    end
end

--------------------------------------------------------------------------------
-- TTLOCK STATE
--------------------------------------------------------------------------------

local blePrepared = false
local scanning = false
local lastScannedMac = nil

--------------------------------------------------------------------------------
-- TTLOCK LISTENER
--------------------------------------------------------------------------------

ttlock.init(function(event)
    if event.message then
        log("Event: " .. tostring(event.message))
    end

    if event.type == "found" then
        log("Found lock: " .. tostring(event.name) .. " (" .. tostring(event.mac) .. ")")
        lastScannedMac = event.mac
    end
end)

--------------------------------------------------------------------------------
-- SAFE BLE HANDLING
--------------------------------------------------------------------------------

local function prepareBLE()
    if blePrepared then return end
    log("Preparing BLE service...")
    ttlock.startBleService()
    blePrepared = true
end

local function startScan()
    if scanning then
        log("Scan already running")
        return
    end

    prepareBLE()

    log("Starting scan...")
    ttlock.startBTDeviceScan()
    scanning = true
end

local function stopScan()
    if not scanning then
        log("Scan not running")
        return
    end

    log("Stopping scan...")
    ttlock.stopBTDeviceScan()
    scanning = false
end

local function connectLock()
    if not lastScannedMac then
        log("No scanned lock to connect")
        return
    end

    log("Initializing lock: " .. lastScannedMac)
    ttlock.lockInitialize()
end

--------------------------------------------------------------------------------
-- BUTTONS
--------------------------------------------------------------------------------

local function makeButton(label, y, handler)
    local btn = widget.newButton({
        label = label,
        x = display.contentCenterX,
        y = y,
        width = 220,
        height = 42,
        shape = "roundedRect",
        cornerRadius = 8,
        onRelease = handler
    })
    group:insert(btn)
end

local BTN_Y = display.contentHeight - 180
local BTN_GAP = 48

makeButton("Print Permission Status", BTN_Y, printPermissionStatus)
makeButton("Start Scan", BTN_Y + BTN_GAP, startScan)
makeButton("Stop Scan", BTN_Y + BTN_GAP * 2, stopScan)
makeButton("Init Last Lock", BTN_Y + BTN_GAP * 3, connectLock)

--------------------------------------------------------------------------------
-- INIT
--------------------------------------------------------------------------------

log("App started")
printPermissionStatus()

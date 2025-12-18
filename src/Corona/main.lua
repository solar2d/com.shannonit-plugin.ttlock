local widget = require("widget")
local ttlock = require("plugin.ttlock")

local uiGroup = display.newGroup()

local title = display.newText({
    parent = uiGroup,
    text = "TTLock Scanner",
    x = display.contentCenterX,
    y = 80,
    font = native.systemFontBold,
    fontSize = 26
})

local output = display.newText({
    parent = uiGroup,
    text = "",
    x = display.contentCenterX,
    y = display.contentCenterY,
    width = display.contentWidth - 40,
    height = 200,
    font = native.systemFont,
    fontSize = 16,
    align = "left"
})

local function log(msg)
    output.text = output.text .. msg .. "\n"
end

-- Callback function to handle TTLock plugin events
local function ttlockListener(event)
    if event.mac then
        log("Found lock: " .. event.name .. " (" .. event.mac .. ")")
    elseif event.status then
        log("Status: " .. event.status)
    elseif event.error then
        log("Error: " .. event.error)
    else
        log("Event: " .. tostring(event))
    end
end

-- -----------------------------
-- SCAN BUTTON
-- -----------------------------
local scanBtn = widget.newButton({
    label = "Start Scan",
    x = display.contentCenterX,
    y = display.contentHeight - 100,
    width = 160,
    height = 50,
    shape = "roundedRect",
    cornerRadius = 10,
    onRelease = function()
        log("Scanning for locks...")
        ttlock.startScanLock(ttlockListener)
    end
})
uiGroup:insert(scanBtn)

-- -----------------------------
-- STOP SCAN BUTTON
-- -----------------------------
local stopScanBtn = widget.newButton({
    label = "Stop Scan",
    x = display.contentCenterX,
    y = display.contentHeight - 160,
    width = 160,
    height = 50,
    shape = "roundedRect",
    cornerRadius = 10,
    onRelease = function()
        ttlock.stopScanLock()
        log("Scan stopped.")
    end
})
uiGroup:insert(stopScanBtn)

-- -----------------------------
-- INIT LOCK BUTTON
-- -----------------------------
local initBtn = widget.newButton({
    label = "Init Lock",
    x = display.contentCenterX,
    y = display.contentHeight - 220,
    width = 160,
    height = 50,
    shape = "roundedRect",
    cornerRadius = 10,
    onRelease = function()
        local mac = "00:11:22:33:44:55" -- replace with actual MAC from scan
        ttlock.initLock(mac, ttlockListener)
        log("Initializing lock: " .. mac)
    end
})
uiGroup:insert(initBtn)

-- -----------------------------
-- RESET EKEY BUTTON
-- -----------------------------
local resetEkeyBtn = widget.newButton({
    label = "Reset Ekey",
    x = display.contentCenterX,
    y = display.contentHeight - 280,
    width = 160,
    height = 50,
    shape = "roundedRect",
    cornerRadius = 10,
    onRelease = function()
        local lockData = "LOCK_DATA"  -- replace with actual lockData
        local lockMac = "00:11:22:33:44:55"
        ttlock.resetEkey(lockData, lockMac, ttlockListener)
        log("Resetting Ekey for: " .. lockMac)
    end
})
uiGroup:insert(resetEkeyBtn)

-- -----------------------------
-- RESET LOCK BUTTON
-- -----------------------------
local resetLockBtn = widget.newButton({
    label = "Reset Lock",
    x = display.contentCenterX,
    y = display.contentHeight - 340,
    width = 160,
    height = 50,
    shape = "roundedRect",
    cornerRadius = 10,
    onRelease = function()
        local lockData = "LOCK_DATA"
        local lockMac = "00:11:22:33:44:55"
        ttlock.resetLock(lockData, lockMac, ttlockListener)
        log("Resetting lock: " .. lockMac)
    end
})
uiGroup:insert(resetLockBtn)

-- -----------------------------
-- CLEAR OUTPUT BUTTON
-- -----------------------------
local clearBtn = widget.newButton({
    label = "Clear",
    x = display.contentCenterX,
    y = display.contentHeight - 40,
    width = 120,
    height = 40,
    shape = "roundedRect",
    cornerRadius = 10,
    onRelease = function()
        output.text = ""
    end
})
uiGroup:insert(clearBtn)

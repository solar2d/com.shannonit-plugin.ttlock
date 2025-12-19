local widget = require("widget")
local ttlock = require("plugin.ttlock")

local uiGroup = display.newGroup()

-- Title
local title = display.newText({
    parent = uiGroup,
    text = "TTLock Scanner",
    x = display.contentCenterX,
    y = 80,
    font = native.systemFontBold,
    fontSize = 26
})

-- Output log
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

-- Store the first scanned device MAC
local firstScannedMac = nil

-- Initialize TTLock listener
ttlock.init(function(event)
    if event.mac and event.name then
        log("Found: " .. event.name .. " (" .. event.mac .. ")")
        -- Save the first scanned device MAC
        if not firstScannedMac then
            firstScannedMac = event.mac
        end
    elseif event.error then
        log("Scan Error: " .. event.error)
    elseif event.message then
        log("Event: " .. event.message)
    end
end)

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
        firstScannedMac = nil  -- Reset previous MAC
        ttlock.startScanLock()
    end
})
uiGroup:insert(scanBtn)

-- -----------------------------
-- STOP SCAN BUTTON
-- -----------------------------
local stopBtn = widget.newButton({
    label = "Stop Scan",
    x = display.contentCenterX,
    y = display.contentHeight - 160,
    width = 160,
    height = 50,
    shape = "roundedRect",
    cornerRadius = 10,
    onRelease = function()
        log("Stopping scan...")
        ttlock.stopScanLock()
    end
})
uiGroup:insert(stopBtn)

-- -----------------------------
-- UNLOCK BUTTON
-- -----------------------------
local unlockBtn = widget.newButton({
    label = "Unlock First",
    x = display.contentCenterX,
    y = display.contentHeight - 220,
    width = 160,
    height = 50,
    shape = "roundedRect",
    cornerRadius = 10,
    onRelease = function()
        if firstScannedMac then
            log("Unlocking lock with MAC: " .. firstScannedMac)
            ttlock.lockInitialize(firstScannedMac) -- Initialize first scanned lock
        else
            log("No lock scanned yet!")
        end
    end
})
uiGroup:insert(unlockBtn)

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

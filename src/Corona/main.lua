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

-- -----------------------------
-- Initialize TTLock listener
-- -----------------------------
ttlock.init(function(event)
    if event.type == "found" and event.mac and event.name then
        log("Found: " .. event.name .. " (" .. event.mac .. ")")
    elseif event.message then
        log("Event: " .. event.message)
    elseif event.error then
        log("Scan Error: " .. event.error)
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
        ttlock.startBTDeviceScan()  -- Java function: Start scan
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
        ttlock.stopBTDeviceScan()  -- Java function: Stop scan
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
        log("Initializing last scanned lock...")
        ttlock.lockInitialize()  -- Java function: Uses lastScannedDevice internally
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

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

-- Event listener for TTLock events
local function ttlockListener(event)
    if event.mac and event.name then
        log("Found lock: " .. event.name .. " [" .. event.mac .. "]")
    elseif event.status then
        log("Operation status: " .. event.status)
    elseif event.error then
        log("Error: " .. event.error)
    elseif event.message then
        log("Message: " .. event.message)
    end
end

-- Initialize the plugin
ttlock.init(ttlockListener)

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
-- UNLOCK BUTTON
-- -----------------------------
local unlockBtn = widget.newButton({
    label = "Unlock First",
    x = display.contentCenterX,
    y = display.contentHeight - 160,
    width = 160,
    height = 50,
    shape = "roundedRect",
    cornerRadius = 10,
    onRelease = function()
        log("Unlocking first lock...")
        -- Example: initialize first lock found
        -- Replace with actual MAC from scanning results
        local firstMac = "00:11:22:33:44:55"
        ttlock.initLock(firstMac, ttlockListener)
    end
})

uiGroup:insert(unlockBtn)

-- -----------------------------
-- GET STATUS BUTTON
-- -----------------------------
local statusBtn = widget.newButton({
    label = "Get Status",
    x = display.contentCenterX,
    y = display.contentHeight - 220,
    width = 160,
    height = 50,
    shape = "roundedRect",
    cornerRadius = 10,
    onRelease = function()
        log("Getting lock status...")
        -- Example: resetEkey as status retrieval
        -- Replace with actual lockData and MAC
        local lockData = "lockDataExample"
        local lockMac = "00:11:22:33:44:55"
        ttlock.resetEkey(lockData, lockMac, ttlockListener)
    end
})

uiGroup:insert(statusBtn)

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

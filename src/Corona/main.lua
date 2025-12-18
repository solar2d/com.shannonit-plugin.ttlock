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

-- Initialize TTLock listener
ttlock.init(function(event)
    if event.message then
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
        ttlock.startScanLock(function(event)
            if event.mac and event.name then
                log("Found: " .. event.name .. " (" .. event.mac .. ")")
            elseif event.error then
                log("Scan Error: " .. event.error)
            end
        end)
    end
})

uiGroup:insert(scanBtn)

-- -----------------------------
-- INIT/UNLOCK BUTTON
-- -----------------------------
local unlockBtn = widget.newButton({
    label = "Init Lock",
    x = display.contentCenterX,
    y = display.contentHeight - 160,
    width = 160,
    height = 50,
    shape = "roundedRect",
    cornerRadius = 10,
    onRelease = function()
        log("Initializing lock...")
        local mac = "LOCK_MAC_ADDRESS" -- replace with actual MAC from scan
        ttlock.initLock(mac, function(event)
            if event.status then
                log("Init Success: " .. event.status)
            elseif event.error then
                log("Init Error: " .. event.error)
            end
        end)
    end
})

uiGroup:insert(unlockBtn)

-- -----------------------------
-- RESET EKEY BUTTON
-- -----------------------------
local resetEkeyBtn = widget.newButton({
    label = "Reset EKey",
    x = display.contentCenterX,
    y = display.contentHeight - 220,
    width = 160,
    height = 50,
    shape = "roundedRect",
    cornerRadius = 10,
    onRelease = function()
        log("Resetting EKey...")
        local lockData = "LOCK_DATA" -- replace with actual lock data
        local mac = "LOCK_MAC_ADDRESS" -- replace with actual MAC
        ttlock.resetEkey(lockData, mac, function(event)
            if event.status then
                log("EKey Reset Success: " .. event.status)
            elseif event.error then
                log("EKey Reset Error: " .. event.error)
            end
        end)
    end
})

uiGroup:insert(resetEkeyBtn)

-- -----------------------------
-- RESET LOCK BUTTON
-- -----------------------------
local resetLockBtn = widget.newButton({
    label = "Reset Lock",
    x = display.contentCenterX,
    y = display.contentHeight - 280,
    width = 160,
    height = 50,
    shape = "roundedRect",
    cornerRadius = 10,
    onRelease = function()
        log("Resetting lock...")
        local lockData = "LOCK_DATA" -- replace with actual lock data
        local mac = "LOCK_MAC_ADDRESS" -- replace with actual MAC
        ttlock.resetLock(lockData, mac, function(event)
            if event.status then
                log("Lock Reset Success: " .. event.status)
            elseif event.error then
                log("Lock Reset Error: " .. event.error)
            end
        end)
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

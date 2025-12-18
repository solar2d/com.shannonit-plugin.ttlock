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
        ttlock.scan(function(result)
            log(result)
        end)
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
        log("Unlocking lock...")
        ttlock.unlock(function(result)
            log(result)
        end)
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
        ttlock.getLockStatus(function(result)
            log(result)
        end)
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

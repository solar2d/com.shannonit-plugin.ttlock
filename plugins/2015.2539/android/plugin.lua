local M = {}

local lib = require("plugin.ttlock")

function M.helloWorld()
    return lib.helloWorld()
end

function M.listMethods()
    return lib.listMethods()
end

return M

--- @meta
--- @class minecraft
local minecraft = {}

--- Print a message to the chat.
--- @param message string
function minecraft.say(message) end

--- @param name string
--- @return World
function minecraft.getWorld(name) end

--- @param fn fun(): boolean
function minecraft.onTick(fn) end

return minecraft
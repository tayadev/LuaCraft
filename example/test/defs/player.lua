---@class Player : Entity
local Player = {}

function Player:getName() end

function Player:playSound(position, sound, category, volume, pitch) end

function Player:showTitle(title, subtitle, fadeIn, stay, fadeOut) end
function Player:showActionbar(text) end

function Player:sendMessage(message) end

function Player:setResourcePack(url, hash) end
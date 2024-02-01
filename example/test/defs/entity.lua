---@class Entity
local Entity = {}

function Entity:getPosition() end
function Entity:setPosition(pos) end

function Entity:getWorld() end

function Entity:hasGravity() end
function Entity:setGravity(gravity) end

function Entity:setRotation(yaw, pitch) end

function Entity:getVelocity() end
function Entity:setVelocity(velocity) end

function Entity:getInvulnerable() end
function Entity:setInvulnerable(invulnerable) end

function Entity:getUUID() end
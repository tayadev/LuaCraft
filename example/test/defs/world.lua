--- @meta
--- @class World
local World = {}

--- Get the block at the given position.
--- @param position Position
--- @return Block
function World:getBlock(position) end

--- Set the block at the given position.
--- @param position Position
--- @param block Block
function World:setBlock(position, block) end

return World
local mc = require "minecraft"

mc.say("Heyo!")


local world = mc.getWorld("minecraft:overworld")
-- world:setBlock({x=10, y=114, z=16}, "minecraft:diamond_block")

local block = world:getBlock({x=10, y=115, z=16})

mc.say(block.id)


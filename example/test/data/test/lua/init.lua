local mc = require "minecraft"
local world = require "world"

print("Hello to Console from Lua!")
mc.say("Hello to Chat from Lua!")

local tick = 0

mc.onTick(function()
  tick = tick + 1
  if tick % 20 == 0 then
    world.setblock({x=10, y=114, z=16}, "minecraft:andesite")
  end
end)
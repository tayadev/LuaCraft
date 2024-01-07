local mc = require "minecraft"

mc.say("Hello to Chat from Lua!")

-- lots of events
-- time
-- get/set block
-- particle
-- entity
-- player
-- chat
-- block entity
-- bossbars
-- scoreboard
-- sounds
-- server resource packs?
-- command
-- title
-- save lua data

local world = mc.getWorld("minecraft:overworld")

local lastBlock = world:getBlock({0, 0, 0})

mc.onTick(function()
  local block = world:getBlock({0, 0, 0})

  -- Only say something if the block has changed
  if block.id ~= lastBlock.id then

    if block.id == "minecraft:stone" then
      mc.say("It's stone!")
    else
      mc.say("It's not stone!")
    end

    lastBlock = block
  end

end)
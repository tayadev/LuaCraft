local mc = require "minecraft"

local game_running = false

local world = mc.getWorld("tntrun")

local players = {}

local function join_game(player)
  if game_running then return end
  table.insert(players, player)
  mc.say(player.name .. " joined the game!")
end

local function leave_game(player)
  for _, p in ipairs(players) do
    if p.name == player.name then
      table.remove(players, _)
      mc.say(player.name .. " left the game!")
      return
    end
  end
end

local function start_game()
  if game_running then return end
  game_running = true
  mc.say("Game started!")
end

local function stop_game()
  if not game_running then return end
  game_running = false
  mc.say("Game stopped!")
end

mc.onTick(function()
  if game_running then
    -- remove blocks below players
    for _, player in ipairs(players) do
      local pos = player.position - Vec3:new(0, 1, 0)
      local block = world:getBlock(pos)
      if block.id ~= "minecraft:air" then
        world:setBlock(pos, "minecraft:air")
      end
    end

    -- kill players that fall out of the world
    for _, player in ipairs(players) do
      local pos = player.position
      if pos.y < 0 then
        player.kill()
        players[_] = nil
      end
    end

    -- stop game if only one player is left
    if #players <= 1 then
      stop_game()
    end
  end
end)

-- prevent players from breaking blocks
mc.onBlockBreak(function(player, pos, block)
  return false
end)

-- prevent players from placing blocks
mc.onBlockPlace(function(player, pos, block)
  return false
end)

-- prevent players from attacking
mc.onAttack(function(player, target)
  return false
end)

-- prevent players from interacting with blocks
mc.onInteract(function(player, pos, block)
  return false
end)
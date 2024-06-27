-- This script will complete almost instantly, but the callback will be called after 5 ticks
run_in(5, function()
  print("Hello from Lua!")
end)
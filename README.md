# Taya's Minecraft Lua Scripting API

This mod adds a Lua scripting API to Minecraft Datapacks


## Datapack Structure

The mod loads scripts from the `lua` folder in datapacks
```
(data pack name)
|- pack.mcmeta
|- pack.png
|- data
   |- (namespace)
      |- lua
         |- init.lua
         |- (script).lua
```

If a script is named `init.lua`, it will be run when the datapack is loaded.

## The `/lua` Command

The `/lua` command allows you to run Lua scripts from chat or command blocks.

`/lua <script>`

## Lua API

TBD
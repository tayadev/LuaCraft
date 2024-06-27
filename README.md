# Taya's Minecraft Lua Scripting API
This mod adds a Lua scripting API to Minecraft Datapacks


# Development

## Building
`./gradlew build`

## Testing
`./gradlew test`

## Serve Docs
`docsify serve docs`

## Run Minecraft
`./gradlew runServer`


# Notes


- Have a Lua VM per datapack
  - That means all scripts in a datapack share the same global state
- on reload all vm state is lost
- scripts are run from the `/lua` command
- script runs are blocking, and will be terminated if they don't complete in a certain time

- callbacks are registered in global scope, and called back from LuaCraft, this way the script can complete after callback registration, and the callback chunk can be run later
  - maybe we should preserve the VM state if there are outstanding callbacks, so that the callback can be run with the given enclosing state
# Installation

## Download

Get the latest build from Modrinth:

[Download HomeGUI on Modrinth](https://modrinth.com/mod/homeguimod)

Pick the file that matches your Minecraft version and take the `homegui-<version>.jar`. There is
no installer, the jar is the whole mod.

## Requirements

| Thing | Version |
|---|---|
| Minecraft | 26.2 |
| Fabric Loader | 0.19.3 or newer |
| Fabric API | required |
| Java | 25 |

## Where the mod goes

Put `homegui-<version>.jar` in the `mods` folder of your **server**, or of your game if you play
single player. That is where homes are stored and where teleports happen, so the server side is
the one that matters.

Installing it on the client as well is optional but recommended. With the mod on the client,
`/home` opens the screen. Without it, `/home` still works and prints the list in chat.

## Mod Menu

If Mod Menu is installed, HomeGUI gets a Config button in the Mods list so you can change
settings in game. It is entirely optional, and the mod runs fine without it.

![HomeGUI in the Mod Menu list, with the Config button top right](/screenshots/mod-menu.png)

## First run

Start the game once. HomeGUI writes `config/homegui.json` with the defaults, and creates a
`homegui` folder inside your world save the first time someone saves a home.

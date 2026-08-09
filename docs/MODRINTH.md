<!--
Description for the HomeGUI project page on Modrinth.
Paste the content below the marker into the Modrinth description editor.
Image links are absolute so they resolve outside this repository.
-->

# HomeGUI

**Save the places you care about and walk back into them from one small screen.**

HomeGUI is a lightweight Fabric mod that adds `/sethome` and `/home` to your world or server, and
puts every home on a single screen you can click. Install it on the server and everyone can use
the commands right away, including players on a plain vanilla client. Players who also install it
get the screen, the **H** key, and a button beside their inventory.

![The home screen listing eight homes, with a tooltip showing a home's dimension and coordinates](https://raw.githubusercontent.com/Nighterezi/HomeGUI/main/docs/public/screenshots/home-screen.png)

## Why you might want it

- **One screen, every home.** Click a row and you are there. No remembering names.
- **Rename without retyping.** Fix a typo in place and keep the spot.
- **Names with colour.** Spaces, the vanilla `&` codes, and `&#RRGGBB` for any hex you like.
- **Fair teleports.** Limits, cooldowns, a warmup you can hear, and cancel on move.
- **Vanilla clients welcome.** Every command works without the mod on the client.
- **English and Vietnamese**, picked from the player's own language setting.

## The home screen

Each home is one row: the wide button teleports you, the pencil renames, and `X` deletes after a
second click. Hover a row to see the dimension and coordinates. The list pages once you have more
homes than fit.

| Rename in place | Delete with a confirmation |
|---|---|
| ![Renaming a home, with its colour codes visible in the name field](https://raw.githubusercontent.com/Nighterezi/HomeGUI/main/docs/public/screenshots/rename-home.png) | ![A delete armed on one row, with the confirmation line below the buttons](https://raw.githubusercontent.com/Nighterezi/HomeGUI/main/docs/public/screenshots/delete-home.png) |

The screen never guesses. Every button asks the server, and the server sends back a fresh list, so
what you see is what you actually have.

## Three ways in

`/home` with no name opens it on any client. With the mod installed you can also press **H**, or
turn on the small house button next to your inventory.

![The house button beside the survival inventory, with its tooltip showing](https://raw.githubusercontent.com/Nighterezi/HomeGUI/main/docs/public/screenshots/inventory-button.png)

## Teleports you can tune

A warmup holds the player still before the teleport, counts down on the action bar with a tick
sound, and cancels if they walk off. Cooldowns, cross dimension travel, and the home limit are all
config options, and operators can be allowed to skip them.

![The countdown on the action bar during a warmup](https://raw.githubusercontent.com/Nighterezi/HomeGUI/main/docs/public/screenshots/teleport-warmup.png)

## Commands

| Command | Action |
|---|---|
| `/sethome [name]` | Save or replace a home where you stand |
| `/home [name]` | Teleport there, or open the screen with no name |
| `/delhome [name]` | Delete a home |
| `/homes` | Open the screen, or list your homes in chat |
| `/homegui reload` | Reload the config and the language files |

Names may contain spaces, so the name is always the last thing on the line. Lookups ignore the
colour markup, so `/home base` still finds a home saved as `&#55FFAAbase`.

![Chat replies from /sethome and /home, with the warmup counting down](https://raw.githubusercontent.com/Nighterezi/HomeGUI/main/docs/public/screenshots/chat-commands.png)

## Configuration

Everything lives in `config/homegui.json`, and `/homegui reload` applies it without a restart. If
you have Mod Menu, the same options are editable in game from **Mods > HomeGUI > Config**.

| Page 1 | Page 2 |
|---|---|
| ![The settings screen, first page](https://raw.githubusercontent.com/Nighterezi/HomeGUI/main/docs/public/screenshots/config-page-1.png) | ![The settings screen, second page](https://raw.githubusercontent.com/Nighterezi/HomeGUI/main/docs/public/screenshots/config-page-2.png) |

You can control the home limit, the default home name, cooldown and warmup seconds, cancel on
move and its tolerance, cross dimension travel, overwriting, operator bypass and permission level,
name length, colour codes, rows per page, whether `/home` opens the screen, the chat prefix, the
inventory button, and both sounds with their volume and pitch.

Homes are stored per player and per world, so a world copy carries its homes with it.

## Requirements

| | |
|---|---|
| Minecraft | 26.2 |
| Loader | Fabric Loader 0.19.3 or newer |
| Java | 25 |
| Required | Fabric API |
| Optional | Mod Menu, for the settings screen |

Install it on the server, or in single player. The client side is optional: without it the
commands still work, and the server prints the list in chat instead of opening the screen.

## Links

- [Documentation](https://nighterezi.github.io/HomeGUI/)
- [Source code](https://github.com/Nighterezi/HomeGUI)
- [Issue tracker](https://github.com/Nighterezi/HomeGUI/issues)

MIT licensed. Made by Nighter.

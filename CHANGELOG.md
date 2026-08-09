# Changelog

All notable changes to HomeGUI are recorded here. The format follows
[Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and the project uses
[Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

Nothing yet.

## [1.0.0] - 2026-08-09

First public release, for Minecraft 26.2.

### Added

- `/sethome`, `/home`, `/delhome` and `/homes`, with names that may contain spaces.
- The home screen. One row per home: click to teleport, the pencil to rename in place, `X` twice
  to delete. Rows page once there are more homes than fit, and hovering one shows its dimension
  and coordinates.
- Three ways to open the screen: `/home` with no name, the Open Homes key (H by default, rebindable
  in Options, Controls), and an optional house button beside the survival inventory.
- Colour in home names. The vanilla `&` and `§` codes plus `&#RRGGBB` for any hex. The length limit
  counts visible characters only, and lookups ignore the markup, so `/home base` finds a home saved
  as `&#55FFAAbase`.
- Teleport rules: a home limit per player, a cooldown, a warmup that counts down on the action bar
  with a sound and cancels if the player moves, and a switch for travel between dimensions.
- Permissions, with a configurable level and an operator bypass for the limit and the cooldown.
- `config/homegui.json` for every setting, and `/homegui reload` to apply it without a restart.
- A settings screen reachable through Mod Menu, at Mods > HomeGUI > Config. Mod Menu is optional
  and the mod runs without it.
- Configurable sounds for a successful teleport and for each warmup tick, with volume and pitch.
- Per player, per world storage, so copying a world carries its homes along.
- English and Vietnamese, picked from the player's own game language.
- Vanilla client support. Every command works without the mod installed on the client, and the
  server prints the home list in chat instead of opening the screen.

[Unreleased]: https://github.com/Nighterezi/HomeGUI/compare/v1.0.0...HEAD
[1.0.0]: https://github.com/Nighterezi/HomeGUI/releases/tag/v1.0.0

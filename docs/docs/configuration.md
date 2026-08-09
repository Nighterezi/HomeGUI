---
outline: [2, 3]
---

# Main Config

`config/homegui.json` is created on first launch. Run `/homegui reload` after editing it, or use
**Mods > HomeGUI > Config** if you have Mod Menu.

::: tip
The Mod Menu screen edits **your** config file. On a dedicated server the server's own copy is
what applies, so changing it on your client will not change anything for other players.
:::

## Homes

### maxHomes

Default `3`. How many homes each player may keep.

Operators can go past this when `opBypassLimits` is on.

### defaultHomeName

Default `home`. The name used when a command is run without one, so `/sethome` on its own saves a
home called `home`.

### allowOverwrite

Default `true`. Whether `/sethome` may replace a home that already has that name. With it off,
players have to delete the old one first.

### maxHomeNameLength

Default `24`. Counted in visible characters, so colour codes do not eat into the budget.

### allowColorsInHomeNames

Default `true`. See [Home Names](/features/home-names). With it off, colour codes are quietly
removed when a home is saved.

## Teleporting

### teleportCooldownSeconds

Default `0`, which is off. How long a player must wait between teleports.

### teleportWarmupSeconds

Default `3`. A countdown before the teleport happens, shown on the action bar. Set it to `0` to
teleport instantly.

### cancelWarmupOnMove

Default `true`. Whether moving during the countdown cancels the teleport.

### warmupMoveTolerance

Default `0.5`. How far, in blocks, a player may drift before that counts as moving. Small values
are strict; raise it if players complain about being cancelled while standing still.

### allowCrossDimension

Default `true`. Whether a player may teleport to a home in another dimension.

## Screen

### guiEntriesPerPage

Default `6`, capped at `10`. How many homes fit on one page of the screen.

### openGuiOnBareHomeCommand

Default `true`. Whether `/home` with no name opens the screen. With it off, `/home` always goes
to the default home.

## Messages

### showMessagePrefix

Default `false`. Whether chat messages start with the mod's name.

Off keeps chat clean, which is usually what you want on a server where players use `/home`
constantly. The countdown on the action bar never carries the prefix either way, because there
is no room for it.

The prefix itself is a normal translation key, `homegui.message.prefix`, so you can change the
wording and the colour per language. See [Translations](/docs/translations).

## Your own interface

### showInventoryButton

Default `false`. Whether a house button is added beside your inventory. Off so the mod does not
change the inventory unless you ask it to.

This one is read from your own config file, not the server's. See
[Opening the Screen](/docs/opening-the-screen).

There is no setting for the Open Homes key, because it lives in **Options > Controls** with every
other key mapping. Clear the binding there to turn it off.

## Operators

### opPermissionLevel

Default `2`. See [Permissions](/docs/permissions).

### opBypassLimits

Default `true`. Whether operators ignore the home limit and the cooldown.

## Sounds

Covered on their own page: [Sounds](/docs/sounds).

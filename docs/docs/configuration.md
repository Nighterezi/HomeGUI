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

Default `0`, which is off. A countdown before the teleport happens, shown on the action bar.

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

## Operators

### opPermissionLevel

Default `2`. See [Permissions](/docs/permissions).

### opBypassLimits

Default `true`. Whether operators ignore the home limit and the cooldown.

## Sounds

Covered on their own page: [Sounds](/docs/sounds).

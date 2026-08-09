# Teleport Rules

Out of the box there are two: you may keep three homes, and a teleport makes you stand still for
three seconds first. Everything else here is opt in.

## How many homes

`maxHomes`, `3` by default. Reaching it does not block `/sethome` on a name you already own, only
new ones.

Operators can go past it while `opBypassLimits` is on.

## Cooldown

`teleportCooldownSeconds` is the wait between teleports. The message tells the player how many
seconds are left.

The cooldown starts when the teleport happens, not when it is requested, so a warmup does not
count against it.

## Warmup

`teleportWarmupSeconds`, three seconds by default, holds the player in place before the teleport.
Set it to `0` to teleport the moment the command is run. The remaining seconds
appear on the action bar and tick down once a second, with a sound each time.

With `cancelWarmupOnMove` on, walking away cancels it. `warmupMoveTolerance` decides how far is
too far, in blocks; the default of `0.5` allows for the small drift you get from standing on a
slab or being nudged by a mob.

Turning around does not cancel it. Only moving does.

## Dimensions

`allowCrossDimension`, on by default, lets players travel between the Overworld, the Nether, the
End and any modded dimension.

Turning it off does not hide those homes. The player still sees them in the list; the teleport is
refused with a message when they are somewhere else.

A home whose dimension no longer exists, because a datapack or mod was removed, is kept and
reports the world as missing.

## Operators

`opBypassLimits` skips the home limit, the cooldown and the warmup in one go. The dimension rule
still applies to everyone.

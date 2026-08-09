# FAQ

## Do players need the mod installed?

No. Everything works through chat commands on a vanilla client. The mod on the client only adds
the screen.

## Can a home be in the Nether or the End?

Yes, and travelling between dimensions is allowed by default. Set `allowCrossDimension` to
`false` to keep players inside the world they are already in.

## What happens to homes in a world that no longer exists?

The home stays in the file, and trying to use it says the world is gone. Delete it with
`/delhome` if you no longer want it.

## Do operators get more homes?

By default yes: `opBypassLimits` lets anyone at `opPermissionLevel` or above ignore both the home
limit and the cooldown. Turn it off to hold everyone to the same rules.

## Are homes shared between worlds?

No. Homes live inside the world folder, so each save has its own set. See
[Storage](/docs/storage).

## I changed the config and nothing happened

Run `/homegui reload`. It re-reads the config and the language files without a restart.

## A name shows as `&aBase` instead of a colour

Colour codes are only applied when `allowColorsInHomeNames` is on. With it off, the codes are
removed when the home is saved rather than being displayed.

# Permissions

HomeGUI does not add permission nodes. Everything a player needs is available to everyone, and
the only gated command is `/homegui reload`.

## Who counts as an operator

Two config keys decide this:

| Key | Default | Effect |
|---|---|---|
| `opPermissionLevel` | `2` | The vanilla permission level treated as operator |
| `opBypassLimits` | `true` | Whether those operators ignore the home limit and the cooldown |

Permission levels are the vanilla ones, from 0 to 4. Level 2 is what `/op` grants by default and
what most moderator commands require.

```json
{
  "opPermissionLevel": 2,
  "opBypassLimits": true
}
```

To hold operators to the same limits as everyone else while still letting them reload the config,
leave `opPermissionLevel` alone and set `opBypassLimits` to `false`.

## Using a permissions plugin

There is nothing to hook up. If your setup manages Minecraft permission levels, granting a player
the matching level is enough for HomeGUI to treat them as an operator.

# Storage

## Where homes live

```
<world folder>/homegui/<player uuid>.json
```

One file per player, inside the world save. That means each world has its own set of homes, and
copying a world takes the homes with it.

A file looks like this:

```json
{
  "base": {
    "name": "base",
    "dimension": "minecraft:overworld",
    "x": 82.5,
    "y": 69.0,
    "z": -87.3,
    "yaw": 178.4,
    "pitch": 2.1,
    "createdAt": 1786312400000
  }
}
```

The key is the name in lower case with any colour codes removed, which is why `/home Base` and
`/home base` find the same home.

## When it is written

Immediately after every change. A crash cannot lose a home that was already saved.

Files are read once per player and kept in memory while they are online, then dropped when they
disconnect.

## Backups and edits

Copy the `homegui` folder to back it up. Editing a file by hand works, but do it while the server
is stopped: a player who is online has their homes cached in memory and will overwrite your edit
on their next change.

## Moving a player between worlds

Homes do not follow. Copy the player's `<uuid>.json` into the other world's `homegui` folder
while the server is stopped.

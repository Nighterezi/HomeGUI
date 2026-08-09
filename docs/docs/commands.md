---
outline: [2, 3]
---

# Commands

Home names may contain spaces, so the name is always the last thing on the line. Type
`/home my summer house` with no quotes.

Names are also tab completed, and tab completion shows them without any colour codes.

![Chat replies from /sethome and /home, with the warmup counting down](/screenshots/chat-commands.png)

## Player commands

### /sethome

```
/sethome [name]
```

Saves a home where you are standing, facing the same way. Without a name it uses
`defaultHomeName` from the config, which is `home` by default.

Saving over an existing name replaces it, unless `allowOverwrite` is turned off.

### /home

```
/home [name]
```

With a name, teleports you straight there.

Without a name it opens the [home screen](/features/home-screen). If your client does not have
the mod, it teleports you to your default home instead, or prints your list if you do not have
one.

### /delhome

```
/delhome [name]
```

Deletes a home. There is no confirmation from chat; the confirmation step exists on the screen.

### /homes

```
/homes
```

Opens the screen, or prints the list in chat for clients without the mod.

## Admin commands

### /homegui reload

```
/homegui reload
```

Re-reads `config/homegui.json` and the language files. Nothing restarts, and players keep their
homes and their open screens.

Requires `opPermissionLevel`, which is permission level 2 by default. See
[Permissions](/docs/permissions).

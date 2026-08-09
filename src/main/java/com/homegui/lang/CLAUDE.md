# lang/

Every word a player can read. No literal text goes to a player anywhere else in the mod.

## Two ways text reaches a player

| Where it shows | Resolved by | How |
|---|---|---|
| Chat and action bar | The **server** | `Localization.prefixed` / `Localization.message` |
| The screens | The **client** | `Component.translatable(Lang.SOME_KEY)` |

Both read the same files. They differ because a vanilla client has no copy of them: it can render
`Component.translatable` for keys it does not know only as the raw key, so anything sent to chat
has to arrive already translated. `Localization` does that using
`ServerPlayer.clientInformation().language()`, the language the client reported when it connected.

Consequence: **a message key is useless on the screen and a screen key is useless in chat**, not
because of any check, but because they are written for different resolvers. Chat keys carry `&`
markup; screen keys carry none.

## Localization

`load()` lists `assets/homegui/lang/*.json` through the Fabric `ModContainer` and reads every file
it finds, so a new language needs no code change. If enumeration fails it falls back to loading
`en_us.json` off the classpath, which is why `DEFAULT_LANGUAGE` must always exist in the jar.

Lookup order for a key: the exact language, then any shipped language with the same prefix
(`en_gb` finds `en_us`), then `en_us`, then the key itself.

`prefixed(...)` is what almost everything wants; it puts `Lang.PREFIX` in front. `message(...)`
without the prefix is for the action bar, where a prefix would waste the width.

### format() is hand written on purpose

`String.format` cannot take a `Component`, and a coloured home name has to be one because hex
colours are a `Style`, not a code. So `format` walks the template itself, splits at `%s`, and
appends each piece as a child.

It also **carries the active style from one piece to the next**. This is the part that is easy to
break: `Component.translatable` does not do this, so a template like `"§7X %s Y %s"` renders only
`X ` grey. Templates in the message files rely on `Localization` behaving like a plain string.
If you ever route a message key through `Component.translatable` instead, its colours will fall
apart.

Only `%s` is supported. `%d` renders literally.

## The files

`en_us.json` is the reference. A missing key falls back to it rather than failing, so a partial
translation degrades quietly. There is no compile-time check and no test: **a new key must be
added to every file**, and `Lang` must get a constant for it.

Message values use `&` codes and `&#RRGGBB`, parsed by
[ColorCodes](../util/ColorCodes.java). Screen values have no markup at all, because the client
resolves them and would print the codes verbatim; screen colours are applied in Java with
`withStyle`.

Key prefixes map to where the text appears:

| Prefix | Shown in |
|---|---|
| `homegui.message.` | Chat and the action bar, server resolved |
| `homegui.gui.` | The home dialog |
| `homegui.config.` | The Mod Menu settings screen |

## Reloading

`/homegui reload` calls `Localization.load()` alongside `HomeGuiConfig.load()`. Nothing is cached
beyond the tables themselves, so edits appear immediately. A screen already open keeps whatever
the client resolved, which is fine because the client resolves it per frame.

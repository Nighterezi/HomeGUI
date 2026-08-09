# docs/

VitePress site for players and server owners. English at the root, Vietnamese under `vi/`.

```bash
npm install
npm run docs:dev      # local preview
npm run docs:build    # must pass before you call a docs change done
```

## Layout

| Path | Holds |
|---|---|
| `index.md`, `vi/index.md` | The hero page. Feature cards link into the two sections below. |
| `docs/` | The manual: installation, commands, config, storage, translations, FAQ. |
| `features/` | What the mod does and why, in prose. No option tables. |
| `.vitepress/config.mts` | Nav and both sidebars. |
| `.vitepress/theme/` | Default theme plus `custom.css` for the brand colour. |

`docs/` answers "how do I set this up". `features/` answers "what is this like to use". A page
that starts listing config keys belongs in `docs/`.

## Adding a page

Two edits, or the page exists but nothing links to it: create the markdown, then add it to the
matching sidebar array in `config.mts`. **Every page needs both an English and a Vietnamese
version**, and both sidebars are separate arrays.

Vietnamese links are absolute and carry the prefix: `/vi/docs/commands`, not `/docs/commands`.

## Style

Written for a server owner who has never seen the code. That means:

- Plain language. Say what a setting does to the game, not which class reads it.
- No Java identifiers, no file paths inside `src/`, no mention of Mojang mappings.
- Short sentences. A default and a one-line reason beats a paragraph.
- Config keys as `### keyName` with the default in the first line, so the on-page outline becomes
  a usable index.
- **No em dashes or en dashes.** Use a comma, a period, or "to" for a range. Check with a ripgrep
  for `[—–]` before finishing.

Tell people what happens when something goes wrong, not just the happy path. A bad sound id being
logged once and ignored is exactly the sort of thing that belongs on the page.

## Keeping it true

The docs describe real behaviour, so a change to
[HomeGuiConfig](../src/main/java/com/homegui/config/HomeGuiConfig.java) or to a command means a
docs edit in the same pass. The pages most likely to go stale:

| Changed | Update |
|---|---|
| A config field | `docs/configuration.md`, or `docs/sounds.md` for the sound keys |
| A command or its arguments | `docs/commands.md` |
| Anything in `HomeScreen` | `features/home-screen.md` |
| Name validation or `ColorCodes` | `features/home-names.md` |
| Cooldown, warmup, dimension rules | `features/teleport-rules.md` |
| The storage path or file format | `docs/storage.md` |

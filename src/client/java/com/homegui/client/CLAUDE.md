# client/

Client-only source set. Two screens and the receiver that opens one of them.

| File | What it is |
|---|---|
| `HomeGuiClient` | Client entrypoint. Registers one payload receiver, nothing else. |
| `HomeScreen` | The dialog `/home` opens. |
| `HomeListData` | The server's JSON payload, unpacked. |
| `ConfigScreen` | The Mod Menu settings editor. |
| `HomeGuiModMenu` | The `modmenu` entrypoint. Only Mod Menu ever loads it. |

## The screen owns no state

`HomeScreen` draws whatever the last `HomeListPayload` said. Clicking a button sends a
`HomeActionPayload` and **changes nothing locally**; the server acts and pushes a fresh list,
which lands in `update()`. Do not optimise this into a local edit. The server is the only place
that knows about limits and name clashes, and a local edit would show a change that was refused.

The exceptions are purely visual and reset on every update: `pendingDelete` (the armed delete),
`renaming` (which home the name field is editing) and `inputText`.

`inputText` lives on the screen rather than in the `EditBox` because `rebuildWidgets()` throws the
widget away. The box gets a responder that writes back into it.

The currently open screen is tracked in a static `opened`, set in `init()` and cleared in
`removed()`. That is how `openOrUpdate` finds it without asking Minecraft for the current screen.

## Rendering in 26.2

`Screen#render` is gone. Two hooks matter and their order is the whole trick:

| Hook | Runs | Use for |
|---|---|---|
| `extractBackground` | Before widgets | The panel backdrop |
| `extractRenderState` | After widgets | Text drawn on top |

Drawing the panel in `extractRenderState` puts it over the buttons. Call `super` first in both.

Text goes through `graphics.text(font, component, x, y, argb, shadow)` or `centeredText`. Colours
are ARGB ints, and the alpha byte is not optional: `0xFFFFFF` is invisible.

## Layout

Every offset is a named constant at the top of the class, and positions are derived from them.
`HomeScreen` computes the panel height from the row count, then places pagination, the footer,
the Close button and the hint line from that, in order. A hard-coded pixel in a `bounds(...)`
call will silently overlap something the next time a row is added; that has already happened once
with the hint line landing on top of Close.

`ConfigScreen` pages at `ROWS_PER_COLUMN * COLUMNS`. Two columns at 224 px is the widest that
fits a 640 px GUI, which is what most players get at their default scale. Adding options is free
because it pages; widening the layout is not.

Option rows hold their own pending value, so paging away and back does not lose an edit, and
nothing is written until Save.

## The pencil icon

`SpriteIconButton` with a sprite from
`assets/homegui/textures/gui/sprites/icon/pencil.png`, referenced as `homegui:icon/pencil`. Files
under `textures/gui/sprites/` are stitched into the GUI atlas automatically.

There is a second `pencil_active.png` used while a rename is in progress. A Unicode glyph was
rejected for this: it would depend on the unifont fallback and on whatever resource pack the
player has.

## Tooltips

`Button.builder(...).tooltip(Tooltip.create(component))`. The home row uses one to carry the
dimension and coordinates so the button itself only has to fit the name.

Build the tooltip as a parent with `withStyle(ChatFormatting.GRAY)` and plain children. Putting
`§7` in the translation template does not work: each argument starts a new sibling from the parent
style, so the colour stops at the first `%s`.

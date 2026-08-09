# client/

Client-only source set. Two screens and the receiver that opens one of them.

| File | What it is |
|---|---|
| `HomeGuiClient` | Client entrypoint. The payload receiver, the key and the inventory button. |
| `HomeScreen` | The dialog `/home` opens. |
| `HomeListData` | The server's JSON payload, unpacked. |
| `ConfigScreen` | The Mod Menu settings editor. |
| `HomeGuiModMenu` | The `modmenu` entrypoint. Only Mod Menu ever loads it. |
| `HomeRequest` | Asks the server to open the screen, guarded by `canSend`. |
| `HomeKeybind` | The Open Homes key, H by default. |
| `InventoryButton` | The house button beside the survival inventory. |
| `mixin/` | One accessor, for the inventory panel position. |

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
because it pages; widening the layout is not. The title and the page counter sit at fixed offsets
above the first row, and they have collided before, so move both together.

Option rows hold their own pending value, so paging away and back does not lose an edit, and
nothing is written until Save.

There is no row for the Open Homes key. `HomeKeybind` registers it with `KeyMappingHelper`, which
puts it in Options, Controls, and that screen is where a player turns it off by clearing the
binding.

## The pencil icon

`SpriteIconButton` with a sprite from
`assets/homegui/textures/gui/sprites/icon/pencil.png`, referenced as `homegui:icon/pencil`. Files
under `textures/gui/sprites/` are stitched into the GUI atlas automatically.

There is a second `pencil_active.png` used while a rename is in progress. A Unicode glyph was
rejected for this: it would depend on the unifont fallback and on whatever resource pack the
player has.

## Opening the screen without a command

The client cannot build the screen by itself, so `HomeKeybind` and `InventoryButton` both send
`HomeActionPayload.OPEN` through `HomeRequest` and wait for the list to come back. Guard anything
new the same way: `HomeRequest.available()` is false on a server without the mod, and the button
is simply not added in that case rather than added and dead.

The key mapping has no config option on purpose: it appears in Options, Controls like any other
key, and clearing the binding there is how a player turns it off. `consumeClick()` is drained
every tick, including on the title screen, so presses cannot queue up and fire later.

`InventoryButton` re-checks the config on every `AFTER_INIT`, so toggling it applies the next
time the inventory opens. It needs `leftPos` and `topPos`, which are protected with no getter,
hence the one accessor mixin; the panel moves when the recipe book opens, so hard-coding the
centred position would be wrong exactly when it is most visible.

## Tooltips

`Button.builder(...).tooltip(Tooltip.create(component))`. The home row uses one to carry the
dimension and coordinates so the button itself only has to fit the name.

Build the tooltip as a parent with `withStyle(ChatFormatting.GRAY)` and plain children. Putting
`§7` in the translation template does not work: each argument starts a new sibling from the parent
style, so the colour stops at the first `%s`.

# Home Screen

`/home` with no name opens it. So does `/homes`.

## A row per home

Each home is one row.

| Control | What it does |
|---|---|
| The wide button | Teleports you there and closes the screen |
| Pencil icon | Starts renaming |
| `X` | Deletes, after a second click |

The wide button shows only the name, so a long one is not squeezed. Hover it to see which
dimension the home is in and its coordinates.

## Renaming

Click the pencil. The current name is loaded into the field at the bottom, the icon lights up,
and the **Set Home** button becomes **Rename**. Edit the name, press Rename, done.

Clicking the pencil again cancels. So does closing the screen.

A rename keeps the home exactly where it is, both in the world and in the list. Renaming onto a
name another home already uses is refused rather than quietly replacing it.

## Deleting

The first click on `X` arms it and turns it red, and a line under the buttons says which home is
about to go. A second click deletes it. Clicking anything else, or paging, disarms it.

## Adding a home

Type a name in the field at the bottom and press **Set Home**. It saves where you are standing,
the same as `/sethome`.

Leaving the field empty uses the default name from the config.

## Pages

`<` and `>` appear once you have more homes than fit on one page. `guiEntriesPerPage` controls
how many that is.

With a single page, the screen shrinks to the homes you actually have instead of leaving empty
rows.

## Without the mod on the client

The screen never opens, and nothing breaks. The server notices and prints the list in chat
instead. Renaming is the one thing that only exists on the screen.

# Opening the Screen

Three ways in. All of them ask the server for your list and open the same screen, so they behave
identically.

| Way | Needs |
|---|---|
| `/home` with no name | Nothing. Works on any client. |
| The **H** key | The mod on your client |
| The button beside your inventory | The mod on your client |

The key and the button do nothing on a server that does not have HomeGUI installed. The button
is not drawn at all in that case, rather than sitting there doing nothing.

## The key

Bound to **H** out of the box. Rebind it under **Options > Controls > Miscellaneous > Open
Homes**, the same as any other key.

There is no config option for it. Clearing the binding on that screen is how you turn it off,
because that is where you would look for a key anyway.

## The inventory button

Off by default. Turn it on with `showInventoryButton`, and a small house button appears to the
right of your inventory panel while it is open. It follows the panel, so opening the recipe book
does not leave it stranded.

The change applies the next time you open your inventory; no restart needed.

Only the survival inventory gets the button. The creative menu does not.

## Which client setting lives where

`showInventoryButton` describes **your own interface**, so it is read from your own
`config/homegui.json`. A server keeps it in its copy too, where it does nothing. The key is not
in the config file at all; Minecraft stores it with your other key bindings.

Everything else, including how many homes you may have, comes from the server.

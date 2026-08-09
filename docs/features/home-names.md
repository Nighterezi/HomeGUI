# Home Names

## Spaces

Names can contain spaces:

```
/sethome my summer house
/home my summer house
```

No quotes needed. The name is always the last thing on the line, which is what makes this work.

## Colour

Names can be coloured with the codes players already know:

| Markup | Example | Result |
|---|---|---|
| `&` and a code | `&asummer house` | Green |
| `§` and a code | `§csummer house` | Red |
| `&#RRGGBB` | `&#55FFAAsummer house` | Any colour at all |

Formatting codes work too: `&l` for bold, `&o` for italic, `&r` to go back to plain.

Hex is real hex, not an approximation to the nearest of the sixteen vanilla colours.

## The name you type is not the name you see

Lookups ignore colour entirely. A home saved as `&#55FFAAbase` is still found by typing:

```
/home base
```

Tab completion offers the plain name for the same reason. This also means two homes cannot differ
only by colour; as far as the mod is concerned they would be the same home.

## Length

`maxHomeNameLength` counts visible characters, so decorating a name never costs you room. A
24 character limit means 24 letters, however many codes you wrap around them.

## Turning it off

Set `allowColorsInHomeNames` to `false`. Codes are then removed as the home is saved rather than
rejected, so a player typing `&abase` simply gets a home called `base`.

## What is not allowed

Nothing else is restricted, beyond an empty name and characters no font can draw. A leftover `§`
that is not part of a valid code is rejected, so nobody can smuggle formatting past the setting
above.

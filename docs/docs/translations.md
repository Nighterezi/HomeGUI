# Translations

HomeGUI ships English and Vietnamese. Each player sees the mod in whatever language their game is
set to, without anyone configuring anything.

## How the language is chosen

Two things happen at once.

The **screen** asks the client, which resolves the text against its own resource packs. That is
plain vanilla behaviour, and it follows the language picker in the game options.

**Chat messages** are translated on the server instead, using the language the client reported
when it connected. This is why a player on a vanilla client, who has no copy of the language
files at all, still gets messages in their own language.

A language the mod does not ship falls back sensibly: `en_gb` gets English, and anything else
unknown gets English too.

## The files

```
src/main/resources/assets/homegui/lang/
  en_us.json
  vi_vn.json
```

`en_us.json` is the reference. A key missing from another file falls back to the English one, so a
half-finished translation degrades instead of showing raw keys.

## Adding a language

Drop another file in that folder named after the language code, such as `de_de.json`, and copy
the contents of `en_us.json` into it before translating. Nothing else needs changing: the mod
lists the folder at startup and picks up whatever is there.

`/homegui reload` re-reads the files, so you can edit and check without restarting.

## Colours in messages

Chat messages use `&` codes, plus `&#RRGGBB` for any other colour:

```json
{
  "homegui.message.home_deleted": "Deleted home &a%s&r.",
  "homegui.message.home_not_found": "&#FF4444You have no home named &f%s&#FF4444."
}
```

The screen's own text has no colour codes in it. Those colours are set by the mod, so translators
only ever deal with wording.

## Placeholders

Values are inserted at each `%s`, in the order they appear. Keep the same number of them as the
English file, in the same order, or the message will come out wrong.

Use `%s` only. `%d` and the other format specifiers are not supported.

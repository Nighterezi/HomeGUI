# Sounds

HomeGUI plays two sounds, both only to the player who triggered them. Nobody standing nearby
hears somebody else's countdown.

## Settings

| Key | Default | When it plays |
|---|---|---|
| `warmupTickSound` | `minecraft:block.note_block.hat` | Once per second during the countdown |
| `warmupTickSoundVolume` | `0.6` | |
| `warmupTickSoundPitch` | `1.4` | |
| `teleportSound` | `minecraft:entity.enderman.teleport` | On arrival |
| `teleportSoundVolume` | `0.7` | |
| `teleportSoundPitch` | `1.0` | |

Volume goes from `0` to `4`. Above `1` the sound does not get louder, it carries further. Pitch
goes from `0.5` to `2`; anything outside that is pulled back into range.

## Turning one off

Set the id to an empty string:

```json
{
  "warmupTickSound": "",
  "teleportSound": "minecraft:entity.enderman.teleport"
}
```

The countdown still runs and still shows on the action bar, it is just silent.

## Choosing a different sound

Any sound id the game knows works, including ones added by a resource pack. A few that suit this
kind of thing:

| Id | Character |
|---|---|
| `minecraft:block.note_block.hat` | Short, dry tick |
| `minecraft:block.note_block.pling` | Brighter, more noticeable |
| `minecraft:ui.button.click` | Very plain |
| `minecraft:entity.enderman.teleport` | The classic teleport whoosh |
| `minecraft:block.beacon.activate` | Long and ceremonial |
| `minecraft:entity.player.levelup` | Celebratory, gets old fast on a countdown |

A typo is not fatal. An unknown id is written to the log once and then ignored, so a countdown
with a bad id simply plays nothing.

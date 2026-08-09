# HomeGUI

Fabric mod for Minecraft 26.2. Players save places with `/sethome` and return with `/home`;
`/home` on its own opens a client screen listing them. Java 25, Gradle Groovy DSL, ~2.7k LOC
across 20 files. Author: Nighter.

## Source sets

| Source set | What it is | Loaded on |
|---|---|---|
| `src/main` | Commands, storage, config, translation, packets. All the behaviour. | Client and server |
| `src/client` | The two screens and the packet receiver that opens them. | Client only |

Split by `loom.splitEnvironmentSourceSets()` in [build.gradle](build.gradle). `src/client` may
reference `src/main`, never the reverse. Anything a screen and the server both need, such as
[ColorCodes](src/main/java/com/homegui/util/ColorCodes.java) and
[Lang](src/main/java/com/homegui/lang/Lang.java), lives in `src/main`.

Mod Menu is a compile and dev-runtime dependency only. It is not in `depends` in
[fabric.mod.json](src/main/resources/fabric.mod.json), and
[HomeGuiModMenu](src/client/java/com/homegui/client/HomeGuiModMenu.java) is reached solely
through the `modmenu` entrypoint, which nothing but Mod Menu loads. Do not import Mod Menu
classes anywhere else.

## Build and run

```bash
./gradlew build
```

Produces `build/libs/homegui-<version>.jar`. Ignore the `-sources.jar`.

```bash
./gradlew runClient
```

Boots a dev client with the mod and Mod Menu on the classpath. There are **no unit tests** in
this repo; verification is manual, in game. The compiler is the only automated check, so build
after every change: this is a post-1.21 Mojang-mapped codebase and the mappings moved a lot in
26.2 (see Invariants).

The VitePress site in `docs/` is separate:

```bash
cd docs && npm install && npm run docs:build
```

## Architecture in one pass

[HomeGui](src/main/java/com/homegui/HomeGui.java) is a thin entrypoint: load config, load
translations, register payloads, register four events, register commands. There is no manager
graph and no dependency injection; everything below it is static.

[HomeService](src/main/java/com/homegui/HomeService.java) holds every behaviour. Both the chat
commands and the GUI buttons call into it, which is the point: limits, cooldowns and messages
cannot drift apart between the two paths. When adding a feature, put the rule in `HomeService`
and give both callers a way in.

Two data flows:

- **Command.** [HomeCommands](src/main/java/com/homegui/command/HomeCommands.java) resolves a
  `ServerPlayer`, calls `HomeService`, which reads and writes through
  [HomeManager](src/main/java/com/homegui/data/HomeManager.java) and messages the player through
  [Localization](src/main/java/com/homegui/lang/Localization.java).
- **Screen.** The server sends `HomeListPayload` (the whole list as JSON). The client parses it
  into [HomeListData](src/client/java/com/homegui/client/HomeListData.java) and builds
  [HomeScreen](src/client/java/com/homegui/client/HomeScreen.java). A button sends
  `HomeActionPayload` back, [HomeNetworking](src/main/java/com/homegui/net/HomeNetworking.java)
  dispatches it to the same `HomeService` method the command would have called, and the server
  pushes a fresh list down.

The screen never mutates anything locally. It draws what the last payload said and waits for the
next one, so there is no client state to keep in sync.

## Where to look

| Task | Location |
|---|---|
| Add or change a command | [command/HomeCommands.java](src/main/java/com/homegui/command/HomeCommands.java) |
| The key mapping or the inventory button | [client/HomeKeybind.java](src/client/java/com/homegui/client/HomeKeybind.java), [client/InventoryButton.java](src/client/java/com/homegui/client/InventoryButton.java) |
| Any rule about homes: limits, cooldown, warmup, validation | [HomeService.java](src/main/java/com/homegui/HomeService.java) |
| Reading or writing home files | [data/HomeManager.java](src/main/java/com/homegui/data/HomeManager.java) |
| The shape of a saved home | [data/Home.java](src/main/java/com/homegui/data/Home.java) |
| A new config option | [config/HomeGuiConfig.java](src/main/java/com/homegui/config/HomeGuiConfig.java), then the Mod Menu screen |
| Wording, translations, the chat prefix | `lang/` (see [lang/CLAUDE.md](src/main/java/com/homegui/lang/CLAUDE.md)) |
| Colour markup in home names | [util/ColorCodes.java](src/main/java/com/homegui/util/ColorCodes.java) |
| Packets between the two sides | [net/](src/main/java/com/homegui/net) |
| The home dialog or the settings screen | `client/` (see [client/CLAUDE.md](src/client/java/com/homegui/client/CLAUDE.md)) |
| Teleporting, permissions, sounds | [util/](src/main/java/com/homegui/util) |
| Sprites and language files | [src/main/resources/assets/homegui/](src/main/resources/assets/homegui) |
| End-user docs | `docs/` (see [docs/CLAUDE.md](docs/CLAUDE.md)) |

## Invariants

**Mojang mappings, and 26.2 moved things.** Yarn is gone. Names that differ from older
Mojang-mapped code: `ResourceLocation` is now `net.minecraft.resources.Identifier`;
`ResourceKey#location()` is `identifier()`; `ServerPlayer#getServer()` no longer exists, use
`player.level().getServer()`; `Minecraft#setScreen` moved to `Minecraft.getInstance().gui`;
`Screen#render` is now `extractRenderState(GuiGraphicsExtractor, ...)` with `extractBackground`
running before widgets. When a symbol will not resolve, run
`javap -classpath <loom deobf jar> <class>` rather than guessing; the jars are under
`~/.gradle/caches/fabric-loom/minecraftMaven/`.

**Permissions are `PermissionCheck`, not integers.** 26.2 replaced numeric levels. Go through
[util/Permissions.java](src/main/java/com/homegui/util/Permissions.java), which maps the config's
number onto `Commands.LEVEL_*`. `ServerPlayer` exposes `permissions()` but does **not** implement
`PermissionSetSupplier`, so pass the `PermissionSet` itself.

**No user-facing string literals.** Every word a player can see is a key in `Lang` resolved from
`assets/homegui/lang/`. Colours are the exception: screen colours are set in code with
`withStyle`, because a translator should only ever deal with wording.

**Legacy `§` codes do not cross component boundaries.** In a `Component.translatable` template,
each literal chunk and each argument is a separate sibling that starts from the parent style, so
`"§7X %s  Y %s"` greys only `X `. Either set the colour on the parent, or use `Localization`,
which carries the style across pieces on purpose. This has already caused one visible bug; do not
reintroduce it by putting codes in a GUI template.

**Home keys are stripped and lower cased.** `Home.key()` runs the name through
`ColorCodes.key()`, so `/home base` finds a home saved as `&#55FFAAbase`. Any new lookup must use
the same key, never `toLowerCase` on the raw name.

**Both sides register payload types.** `HomeNetworking.registerPayloads()` runs from the common
entrypoint. Registering a payload on only one side fails at runtime, not at compile time.

**Writes are immediate.** `HomeManager` saves after every change; there is no dirty flag and no
batching. Do not add a save queue without also handling the disconnect and shutdown paths.

**Adding a config option means four edits.** The field in `HomeGuiConfig`, clamping in
`sanitize()` if it has a valid range, a `Lang` key plus both language files for the label, and a
row in `ConfigScreen.buildOptions()`. Miss the last one and the option is invisible in game.

**A key mapping is not a config option.** The Open Homes key is registered with
`KeyMappingHelper` and appears in Options, Controls; clearing the binding there is how a player
turns it off. Do not add a config field that duplicates a control the game already owns.

**One config option is client only.** `showInventoryButton` is read by whichever side is
running the interface, which on multiplayer is the player's own file. Everything else is a server
decision. Do not read a client-only option from `src/main` code that runs on a server.

## Conventions

- Tabs for indentation, matching the Fabric example mod. Braces on the same line.
- Comments explain *why*, and are rare. Do not narrate what the next line does.
- Javadoc is English. So is every comment, every log line and every identifier.
- **No em dashes or en dashes anywhere**, including docs and language files. Use a comma, a
  period, or "to" for ranges. Check with a ripgrep for `[—–]` before finishing.
- Named constants over magic numbers, especially in the two screens where every offset is
  layout. `HomeScreen` and `ConfigScreen` derive positions from constants; do not hard-code a
  pixel value into a `bounds(...)` call.
- One class per file. `util/` holds a class per concern, each a `final class` with a private
  constructor and static methods.
- `HomeGui.LOGGER` for diagnostics, never `System.out`. A bad value from a config or a language
  file is logged once and degraded past, not thrown.

## Docs and releases

- Version lives in `mod_version` in [gradle.properties](gradle.properties) and is templated into
  `fabric.mod.json` at build time.
- Minecraft, loader, Fabric API and Mod Menu versions are all in `gradle.properties`. Check
  https://fabricmc.net/develop before bumping any of them.
- `docs/` is end-user facing and deliberately plain. It is not a place for API notes.

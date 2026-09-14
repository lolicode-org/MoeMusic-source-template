# MoeMusic Plugin Template

[简体中文](./README_zh.md) | English

Minimal plugin template for [MoeMusic](https://github.com/lolicode-org/MoeMusic).

This repository keeps the template lightweight and independent of heavy Minecraft mod frameworks. The resulting single JAR works simultaneously as:
- A **Minecraft Mod** for **Fabric**, **NeoForge**, and **Minecraft Forge** (installable directly into `.minecraft/mods/`).
- A **Standalone Plugin** for any jvm platform that MoeMusic works on (loaded via Java SPI).

## Contents

- `TemplatePlugin`: plugin identity, config spec, mod ID, and source registration.
- `TemplatePluginProvider`: Java SPI entry point for standalone JAR loading.
- `platform/FabricEntrypoint`: Fabric / Quilt mod entrypoint.
- `platform/NeoForgeEntrypoint`: NeoForge mod entrypoint.
- `platform/ForgeEntrypoint`: Minecraft Forge mod entrypoint.
- `TemplateConfig`: serializable TOML config model.
- `TemplateMusicSource`: minimal searchable and identifier-resolvable source.
- `assets/example/lang/`: bundled translations for the `example` plugin namespace.
- Mod metadata descriptors: `fabric.mod.json`, `META-INF/neoforge.mods.toml`, `META-INF/mods.toml`.

## Rename Before Use

1. Replace `com.example.moemusic.template` with your package across `src/`.
2. Update IDs in `TemplatePlugin.kt` (`PLUGIN_ID`, `CONFIG_ID`, `MOD_ID`, `SOURCE_ID`).
3. Update `gradle.properties` (`mod_id`, `mod_name`, `mod_description`, `mod_author`, `fabric_entrypoint`).
4. Move `assets/example/lang/` to the namespace used by your plugin ID (`assets/<namespace>/lang/`).
5. Update `META-INF/services/org.lolicode.moemusic.api.plugin.PluginProvider` with your provider class.
6. Replace the demo source logic with your real source implementation.

## Build

```bash
./gradlew build
```

The generated `build/libs/*-full.jar` is universal and can be installed into:

- **Minecraft**: drop directly into `.minecraft/mods/` (works with Fabric, Forge, and NeoForge across all supported Minecraft versions).
- **Standalone / Non-Minecraft**: drop into `config/moemusic/plugins/` (or server plugins folder).

Restart the server or client after changing plugin JARs.

## License

This template inherits the repository license, which is AGPL-3.0-or-later due to the core API's license.

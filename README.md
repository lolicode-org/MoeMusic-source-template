# MoeMusic Standalone Plugin Template

[简体中文](./README_zh.md) | English

Minimal standalone plugin template for [MoeMusic](https://github.com/lolicode-org/MoeMusic).

This repository intentionally keeps the template small. For API behavior and contracts, use the MoeMusic API documentation.

## Contents

- `TemplatePlugin`: plugin identity, config spec, and source registration.
- `TemplatePluginProvider`: Java SPI entry point for standalone JAR loading.
- `TemplateConfig`: serializable TOML config model.
- `TemplateMusicSource`: minimal searchable and identifier-resolvable source.
- `assets/example/lang/`: bundled translations for the `example` plugin namespace.

## Rename Before Use

1. Replace `com.example.moemusic.template` with your package.
2. Change `PLUGIN_ID`, `CONFIG_ID`, and `SOURCE_ID` in `TemplatePlugin`.
3. Move `assets/example/lang/` to the namespace used by your plugin ID.
4. Update `META-INF/services/org.lolicode.moemusic.api.plugin.PluginProvider`.
5. Replace the demo source logic with your real source implementation.

## Build

```bash
./gradlew build
```

Install the generated `build/libs/*-full.jar` into:

```text
config/moemusic/plugins/
```

Restart the server or client after changing plugin JARs.

## License

This template inherits the repository license, which is AGPL-3.0-or-later due to the core API's license.

# MoeMusic 插件模板

简体中文 | [English](./README.md)

这是一个面向 [MoeMusic](https://github.com/lolicode-org/MoeMusic) 的插件模板。

本模板保持轻量，不依赖 Minecraft 模组框架。构建输出的单个 JAR 同时作为：
- **Minecraft 模组**：原生支持 **Fabric**、**NeoForge** 和 **Minecraft Forge**（可直接放入 `.minecraft/mods/`）。
- **独立插件**：支持 MoeMusic 本身支持的任意环境（通过 Java SPI 加载）。

## 内容

- `TemplatePlugin`：插件身份、配置声明、模组 ID 和音源注册。
- `TemplatePluginProvider`：独立 JAR 加载使用的 Java SPI 入口。
- `platform/FabricEntrypoint`：Fabric / Quilt 模组入口。
- `platform/NeoForgeEntrypoint`：NeoForge 模组入口。
- `platform/ForgeEntrypoint`：Minecraft Forge 模组入口。
- `TemplateConfig`：可序列化的 TOML 配置模型。
- `TemplateMusicSource`：最小可搜索、可解析标识符的音源示例。
- `assets/example/lang/`：`example` 命名空间的内置翻译。
- 模组元数据清单：`fabric.mod.json`、`META-INF/neoforge.mods.toml`、`META-INF/mods.toml`。

## 使用前需要修改

1. 将 `src/` 中的 `com.example.moemusic.template` 替换为你的包名。
2. 修改 `TemplatePlugin.kt` 中的 `PLUGIN_ID`、`CONFIG_ID`、`MOD_ID` 和 `SOURCE_ID`。
3. 修改 `gradle.properties` 中的元数据（`mod_id`、`mod_name`、`mod_description`、`mod_author`、`fabric_entrypoint`）。
4. 将 `assets/example/lang/` 移到你的插件 ID 使用的命名空间（`assets/<namespace>/lang/`）。
5. 更新 `META-INF/services/org.lolicode.moemusic.api.plugin.PluginProvider` 为你的 Provider 类名。
6. 用真实音源实现替换示例逻辑。

## 构建

```bash
./gradlew build
```

生成的 `build/libs/*-full.jar` 可安装到：

- **Minecraft**：直接放入 `.minecraft/mods/`。
- **独立 / 非 Minecraft 环境**：放入 `config/moemusic/plugins/`（或对应服务端的插件目录）。

修改插件 JAR 后需要重启服务端或客户端。

## 许可证

由于核心 API 使用 AGPL-3.0-or-later 许可证，本模板继承了该许可证。

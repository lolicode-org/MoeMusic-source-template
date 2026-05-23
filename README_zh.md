# MoeMusic 独立插件模板

简体中文 | [English](./README.md)

这是一个面向 [MoeMusic](https://github.com/lolicode-org/MoeMusic) 的最小独立插件模板。

模板只保留必要结构。API 行为与契约请以 MoeMusic API 文档为准。

## 内容

- `TemplatePlugin`：插件身份、配置声明和音源注册。
- `TemplatePluginProvider`：独立 JAR 加载使用的 Java SPI 入口。
- `TemplateConfig`：可序列化的 TOML 配置模型。
- `TemplateMusicSource`：最小可搜索、可解析标识符的音源示例。
- `assets/example/lang/`：`example` 命名空间的内置翻译。

## 使用前需要修改

1. 将 `com.example.moemusic.template` 替换为你的包名。
2. 修改 `TemplatePlugin` 中的 `PLUGIN_ID`、`CONFIG_ID` 和 `SOURCE_ID`。
3. 将 `assets/example/lang/` 移到你的插件 ID 使用的命名空间。
4. 更新 `META-INF/services/org.lolicode.moemusic.api.plugin.PluginProvider`。
5. 用真实音源实现替换示例逻辑。

## 构建

```bash
./gradlew build
```

将生成的 `build/libs/*-full.jar` 放入：

```text
config/moemusic/plugins/
```

修改插件 JAR 后需要重启服务端或客户端。

## 许可证

由于核心 API 使用 AGPL-3.0-or-later 许可证，本模板继承了该许可证。

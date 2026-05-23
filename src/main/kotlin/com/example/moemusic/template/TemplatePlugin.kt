package com.example.moemusic.template

import org.lolicode.moemusic.api.LocalizedText
import org.lolicode.moemusic.api.plugin.Plugin
import org.lolicode.moemusic.api.plugin.PluginConfigSpec
import org.lolicode.moemusic.api.plugin.ServerRuntimeContext
import org.lolicode.moemusic.api.plugin.pluginConfigSpec
import java.net.URI

/**
 * Standalone plugin entry point.
 * [TemplatePluginProvider] exposes this object through Java ServiceLoader.
 */
object TemplatePlugin : Plugin {

    /**
     * Use stable, globally unique IDs. The namespace before ':' is also the lang asset namespace,
     * so this template stores translations under `assets/example/lang/`.
     */
    const val PLUGIN_ID = "example:template-source"

    /** Config IDs are filenames. Keep them lowercase and filesystem-safe. */
    const val CONFIG_ID = "example_template_source"

    /** Source IDs are global across built-in and plugin-provided music sources. Not required if your plugin is not a music source. */
    const val SOURCE_ID = "example:template"

    override val id: String = PLUGIN_ID
    override val configId: String = CONFIG_ID
    override val displayName: LocalizedText = LocalizedText.key("plugin.example.template_source")
    override val version: String = "1.0.0"

    /** Keep this range aligned with the MoeMusic API version your plugin was tested against. */
    override val supportedApiVersions: String = ">=1.0.0 <2.0.0"

    override val configSpec: PluginConfigSpec<TemplateConfig> = pluginConfigSpec(::TemplateConfig) {
        boolean(
            key = "enabled",
            getter = { it.enabled },
            updater = { config, value -> config.copy(enabled = value) },
        )
        string(
            key = "media_base_url",
            getter = { it.mediaBaseUrl },
            updater = { config, value -> config.copy(mediaBaseUrl = value) },
            validator = { _, value -> validateHttpBaseUrl(value) },
        )
    }

    override fun onServerRuntimeLoad(ctx: ServerRuntimeContext) {
        val source = TemplateMusicSource(ctx.loadConfig(configSpec))

        /*
         * Register sources during runtime load, not session load. Integrated servers can create
         * multiple sessions in one JVM, and duplicate source IDs are fatal.
         */
        ctx.registerMusicSource(source)

        /*
         * MoeMusic does not hot-unregister sources. Update runtime state when config changes.
         */
        ctx.onConfigChanged(configSpec) { config ->
            source.updateConfig(config)
        }

        ctx.logger.info("Registered MoeMusic template source '{}'.", SOURCE_ID)
    }

    private fun validateHttpBaseUrl(value: String): LocalizedText? {
        val trimmed = value.trim()
        if (trimmed.isBlank()) {
            return LocalizedText.key("config.example.template.source.validation.media_base_url_blank")
        }

        val uri = runCatching { URI(trimmed) }.getOrNull()
        val scheme = uri?.scheme?.lowercase()
        return if (uri == null || uri.host.isNullOrBlank() || scheme !in setOf("http", "https")) {
            LocalizedText.key("config.example.template.source.validation.media_base_url_http")
        } else {
            null
        }
    }
}

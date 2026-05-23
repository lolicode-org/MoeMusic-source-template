package com.example.moemusic.template

import org.lolicode.moemusic.api.plugin.Plugin
import org.lolicode.moemusic.api.plugin.PluginProvider

/**
 * Java SPI entry point for standalone plugin jars.
 *
 * Requirements from MoeMusic:
 * - public class
 * - public no-argument constructor
 * - listed in `META-INF/services/org.lolicode.moemusic.api.plugin.PluginProvider`
 */
class TemplatePluginProvider : PluginProvider {
    override fun plugins(): Iterable<Plugin> = listOf(TemplatePlugin)
}

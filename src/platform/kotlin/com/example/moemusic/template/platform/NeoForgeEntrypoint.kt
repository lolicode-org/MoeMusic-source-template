package com.example.moemusic.template.platform

import com.example.moemusic.template.TemplatePlugin
import net.neoforged.fml.common.Mod
import org.lolicode.moemusic.api.MoeMusicApi

/**
 * NeoForge mod entrypoint.
 *
 * Instantiated by NeoForge FML during mod loading to register [TemplatePlugin]
 * with MoeMusic's public plugin API.
 */
@Mod(TemplatePlugin.MOD_ID)
class NeoForgeEntrypoint {
    init {
        MoeMusicApi.registerPlugin(TemplatePlugin)
    }
}

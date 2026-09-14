package com.example.moemusic.template.platform

import com.example.moemusic.template.TemplatePlugin
import net.fabricmc.api.ModInitializer
import org.lolicode.moemusic.api.MoeMusicApi

/**
 * Fabric / Quilt mod entrypoint.
 *
 * Called by FabricLoader during game initialization to register [TemplatePlugin]
 * with MoeMusic's public plugin API.
 */
class FabricEntrypoint : ModInitializer {
    override fun onInitialize() {
        MoeMusicApi.registerPlugin(TemplatePlugin)
    }
}

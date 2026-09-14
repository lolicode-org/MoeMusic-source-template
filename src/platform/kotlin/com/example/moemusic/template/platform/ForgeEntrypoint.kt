package com.example.moemusic.template.platform

import com.example.moemusic.template.TemplatePlugin
import net.minecraftforge.fml.common.Mod
import org.lolicode.moemusic.api.MoeMusicApi

/**
 * Minecraft Forge mod entrypoint.
 *
 * Instantiated by Forge FML during mod loading to register [TemplatePlugin]
 * with MoeMusic's public plugin API.
 */
@Mod(TemplatePlugin.MOD_ID)
class ForgeEntrypoint {
    init {
        MoeMusicApi.registerPlugin(TemplatePlugin)
    }
}

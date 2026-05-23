package com.example.moemusic.template

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * TOML-backed plugin config.
 * MoeMusic stores it as `config/moemusic/plugin-configs/<configId>.toml`.
 */
@Serializable
data class TemplateConfig(
    /** Keep toggles in config; sources are registered once and then updated in place. */
    @SerialName("enabled")
    val enabled: Boolean = true,

    /** Example setting used by resolve(); replace it with your real upstream settings. */
    @SerialName("media_base_url")
    val mediaBaseUrl: String = "https://media.example.invalid/moemusic-template",
)

package com.example.moemusic.template

import org.lolicode.moemusic.api.*
import org.lolicode.moemusic.api.model.*
import java.net.URI

class TemplateMusicSource(initialConfig: TemplateConfig = TemplateConfig()) :
    SearchableMusicSource,
    IdentifierResolvableMusicSource {

    @Volatile
    private var config: TemplateConfig = initialConfig

    override val id: String = TemplatePlugin.SOURCE_ID
    override val displayName: LocalizedText = LocalizedText.key("source.example.template")

    fun updateConfig(config: TemplateConfig) {
        this.config = config
    }

    override suspend fun search(query: SearchQuery, submitter: MoeMusicUser?): UserResult<SearchResult> {
        if (!config.enabled) {
            return UserResult.Error(disabledMessage())
        }

        // Return SelectionEntry rows from your upstream search. Empty search is a successful page.
        val entries = if (DEMO_TRACK.title.contains(query.query.trim(), ignoreCase = true)) {
            listOf(DEMO_TRACK.toSelectionEntry())
        } else {
            emptyList()
        }

        return UserResult.Success(
            SearchResult(
                entries = entries,
                sourceId = id,
                total = entries.size,
            )
        )
    }

    override suspend fun resolveIdentifier(
        identifier: String,
        submitter: MoeMusicUser?,
    ): IdentifierResolutionResult {
        val trackId = identifier.removePrefix(IDENTIFIER_PREFIX).takeIf { it != identifier }
            ?: return IdentifierResolutionResult.Pass

        if (trackId.isBlank()) {
            return IdentifierResolutionResult.Blocked(invalidTrackIdMessage())
        }

        return when (val result = getTrackInfo(trackId, submitter)) {
            is UserResult.Success -> result.value
                ?.let(IdentifierResolutionResult::Resolved)
                ?: IdentifierResolutionResult.Blocked(trackNotFoundMessage(trackId))
            is UserResult.Error -> IdentifierResolutionResult.Blocked(result.message)
        }
    }

    override suspend fun getTrackInfo(trackId: String, submitter: MoeMusicUser?): UserResult<TrackInfo?> {
        if (!config.enabled) {
            return UserResult.Error(disabledMessage())
        }
        if (trackId.isBlank() || trackId.any(Char::isWhitespace)) {
            return UserResult.Error(invalidTrackIdMessage())
        }

        // Return Success(null) for well-formed IDs that simply do not exist.
        return UserResult.Success(DEMO_TRACK.takeIf { it.id == trackId }?.toTrackInfo())
    }

    override suspend fun resolve(track: TrackInfo, submitter: MoeMusicUser?): PlaybackResource {
        val currentConfig = config
        if (!currentConfig.enabled) {
            throw TrackUnavailableException(disabledMessage())
        }
        if (track.id.isBlank() || track.id.any(Char::isWhitespace)) {
            throw SourceFormatException()
        }
        if (track.id != DEMO_TRACK.id) {
            throw TrackUnavailableException(trackNotFoundMessage(track.id))
        }

        // Resolve as late as possible. TrackInfo.id should be stable; playback URLs may expire.
        return PlaybackResource(url = "${normalizedBaseUrl(currentConfig.mediaBaseUrl)}/${track.id}.mp3")
    }

    private fun normalizedBaseUrl(rawBaseUrl: String): String {
        val trimmed = rawBaseUrl.trim().trimEnd('/')
        val uri = runCatching { URI(trimmed) }.getOrNull()
        val scheme = uri?.scheme?.lowercase()
        if (uri == null || uri.host.isNullOrBlank() || scheme !in setOf("http", "https")) {
            throw SourceException(LocalizedText.key("error.example.template.invalid_config"))
        }
        return trimmed
    }

    private fun disabledMessage(): LocalizedText =
        LocalizedText.key("error.example.template.disabled")

    private fun invalidTrackIdMessage(): LocalizedText =
        LocalizedText.key("error.example.template.invalid_track_id")

    private fun trackNotFoundMessage(trackId: String): LocalizedText =
        LocalizedText.key("error.example.template.track_not_found", trackId)

    private companion object {
        const val IDENTIFIER_PREFIX = "example:"

        val DEMO_TRACK = TemplateTrack(
            id = "demo-track",
            title = "Demo Track",
            artist = "Example Artist",
            durationMs = 180_000,
        )
    }
}

private data class TemplateTrack(
    val id: String,
    val title: String,
    val artist: String,
    val durationMs: Long,
) {
    fun toTrackInfo(): TrackInfo =
        TrackInfo(
            id = id,
            title = title,
            artists = listOf(ArtistInfo.fromName(artist)),
            durationMs = durationMs,
            sourceId = TemplatePlugin.SOURCE_ID,
        )

    fun toSelectionEntry(): SelectionEntry =
        SelectionEntry(
            selectionId = id,
            title = title,
            artists = listOf(ArtistInfo.fromName(artist)),
            durationMs = durationMs,
            sourceId = TemplatePlugin.SOURCE_ID,
            kind = SelectionEntryKind.TRACK,
        )
}

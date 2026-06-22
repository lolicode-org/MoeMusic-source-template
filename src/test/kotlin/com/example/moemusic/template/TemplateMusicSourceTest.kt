package com.example.moemusic.template

import kotlinx.coroutines.runBlocking
import org.lolicode.moemusic.api.IdentifierResolutionResult
import org.lolicode.moemusic.api.LocalizedText
import org.lolicode.moemusic.api.TrackUnavailableException
import org.lolicode.moemusic.api.UserResult
import org.lolicode.moemusic.api.model.SearchQuery
import org.lolicode.moemusic.api.model.SearchResult
import org.lolicode.moemusic.api.model.SelectionEntryKind
import org.lolicode.moemusic.api.model.TrackInfo
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

class TemplateMusicSourceTest {

    @Test
    fun `search returns a direct track entry`() = runBlocking {
        val result = TemplateMusicSource().search(SearchQuery(query = "demo"), submitter = null)

        val success = assertIs<UserResult.Success<SearchResult>>(result)
        val searchResult = success.value
        assertEquals(1, searchResult.total)
        assertEquals(SelectionEntryKind.TRACK, searchResult.entries.single().kind)
        assertEquals(TemplatePlugin.SOURCE_ID, searchResult.entries.single().sourceId)
    }

    @Test
    fun `identifier resolver passes unrelated input`() = runBlocking {
        val result = TemplateMusicSource().resolveIdentifier("other:demo-track", submitter = null)

        assertEquals(IdentifierResolutionResult.Pass, result)
    }

    @Test
    fun `identifier resolver resolves owned input`() = runBlocking {
        val result = TemplateMusicSource().resolveIdentifier("example:demo-track", null)

        val resolved = assertIs<IdentifierResolutionResult.Resolved>(result)
        assertEquals("demo-track", resolved.track.id)
    }

    @Test
    fun `identifier resolver blocks missing track for owned prefix`() = runBlocking {
        val result = TemplateMusicSource().resolveIdentifier("example:missing", submitter = null)

        val blocked = assertIs<IdentifierResolutionResult.Blocked>(result)
        assertEquals(LocalizedText.key("error.example.template.track_not_found", "missing"), blocked.message)
    }

    @Test
    fun `disabled source blocks owned identifiers`() = runBlocking {
        val source = TemplateMusicSource(TemplateConfig(enabled = false))

        assertIs<IdentifierResolutionResult.Blocked>(source.resolveIdentifier("example:demo-track", null))
        Unit
    }

    @Test
    fun `getTrackInfo returns null for unknown valid ids`() = runBlocking {
        val result = TemplateMusicSource().getTrackInfo("unknown-track", submitter = null)

        assertEquals(UserResult.Success(null), result)
    }

    @Test
    fun `resolve builds a fresh playback resource from config`() = runBlocking {
        val source = TemplateMusicSource(
            TemplateConfig(mediaBaseUrl = "https://cdn.example.invalid/audio/"),
        )
        val playback = source.resolve(
            TrackInfo(
                id = "demo-track",
                title = "Old title from queue",
                artists = emptyList(),
                durationMs = 1) {
                    sourceId = TemplatePlugin.SOURCE_ID
                },
            submitter = null,
        ).playback

        assertEquals("https://cdn.example.invalid/audio/demo-track.mp3", playback.url)
    }

    @Test
    fun `resolve reports stale known-source ids as unavailable`() = runBlocking {
        val source = TemplateMusicSource()

        val error = assertFailsWith<TrackUnavailableException> {
            source.resolve(
                TrackInfo(
                    id = "missing",
                    title = "Missing",
                    artists = emptyList(),
                    durationMs = 1) {
                        sourceId = TemplatePlugin.SOURCE_ID
                    },
                submitter = null,
            )
        }

        assertEquals(LocalizedText.key("error.example.template.track_not_found", "missing"), error.userMessage)
    }
}

class TemplatePluginConfigTest {

    @Test
    fun `base url validator requires http url`() {
        val entry = TemplatePlugin.configSpec.entries.single { it.key == "media_base_url" }

        val message = entry.validate(TemplateConfig(), "file:///tmp/song.mp3")

        assertEquals(
            LocalizedText.key("config.example.template.source.validation.media_base_url_http"),
            message,
        )
    }
}

package pl.michalboryczko.exercise.model.api

import pl.michalboryczko.exercise.model.ChannelSimple

data class Channel(
        val kind: String,
        val etag: String,
        val id: Id,
        val snippet: Snippet
)

fun Channel.convertToChannelSimple(): ChannelSimple
    = this.snippet.let { ChannelSimple(it.title, it.description, it.thumbnails.default.url) }
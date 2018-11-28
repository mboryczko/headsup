package pl.michalboryczko.exercise.model.api

data class ChannelResponse(
        val kind: String,
        val etag: String,
        val nextPageToken: String,
        val regionCode: String,
        val pageInfo: PageInfo,
        val items: List<Channel>
)
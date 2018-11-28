package pl.michalboryczko.exercise.model.api

data class Thumbnails(
        val default: ThumbnailDetails,
        val medium: ThumbnailDetails,
        val high: ThumbnailDetails
)
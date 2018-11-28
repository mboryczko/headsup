package pl.michalboryczko.exercise.source.api

import io.reactivex.Single
import pl.michalboryczko.exercise.model.api.ChannelResponse
import retrofit2.http.GET

interface Api {

    @GET("/sciTube/v2/channels.json")
    fun getChannels() : Single<ChannelResponse>

}
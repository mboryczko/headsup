package pl.michalboryczko.exercise.source.repository

import io.reactivex.Single
import pl.michalboryczko.exercise.model.api.ChannelResponse

interface ChannelRepository {
    fun getChannels(): Single<ChannelResponse>

}
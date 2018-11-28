package pl.michalboryczko.exercise.source.api

import android.content.Context
import io.reactivex.Single
import pl.michalboryczko.exercise.model.api.ChannelResponse
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class ApiService
@Inject constructor(var context: Context, var endpoint: String, var api: Api)
{
    fun getChannels(): Single<ChannelResponse> {
        return api.getChannels()
    }

}
package pl.michalboryczko.exercise.source.api.rest

import io.reactivex.Single
import pl.michalboryczko.exercise.model.api.CurrencyPairResponse
import pl.michalboryczko.exercise.model.api.CurrencyTickerResponse
import retrofit2.http.GET

interface Api {

    @GET("/api/2/public/symbol")
    fun getCurrencyPairs() : Single<List<CurrencyPairResponse>>


    @GET("/api/2/public/ticker")
    fun getCurrencyTickers() : Single<List<CurrencyTickerResponse>>

}
package pl.michalboryczko.exercise.source.api.rest

import android.content.Context
import io.reactivex.Single
import pl.michalboryczko.exercise.model.api.CurrencyPairResponse
import pl.michalboryczko.exercise.model.api.CurrencyTickerResponse
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class RestApiService
@Inject constructor(
        private val context: Context,
        private val api: Api)
{
    fun getCurrencyPairs(): Single<List<CurrencyPairResponse>>
        = api.getCurrencyPairs()

    fun getCurrencyTickers() : Single<List<CurrencyTickerResponse>>
        = api.getCurrencyTickers()

}
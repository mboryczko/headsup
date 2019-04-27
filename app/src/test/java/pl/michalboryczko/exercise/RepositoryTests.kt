package pl.michalboryczko.exercise

import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import pl.michalboryczko.exercise.model.exceptions.NoInternetException
import pl.michalboryczko.exercise.source.api.InternetConnectivityChecker
import pl.michalboryczko.exercise.source.api.rest.RestApiService
import pl.michalboryczko.exercise.source.CryptocurrencyMapper
import pl.michalboryczko.exercise.source.repository.RepositoryImpl
import pl.michalboryczko.exercise.source.api.websocket.WebSocketApiService

class RepositoryTests: BaseTest() {


    private val restApi = Mockito.mock(RestApiService::class.java)
    private val webSocketService = Mockito.mock(WebSocketApiService::class.java)
    private val repo by lazy { RepositoryImpl(restApi, webSocketService, CryptocurrencyMapper(), checker) }

    @Before
    override fun setUp(){
        whenever(restApi.getCurrencyPairs()).thenReturn(Single.just(listOf(pairResponseMock)))
        whenever(restApi.getCurrencyTickers()).thenReturn(Single.just(listOf(tickerResponseMock)))
    }

    @Test
    fun getCryptocurrencyPairsTest(){
        repo.getCryptocurrencyPairs()
                .test()
                .assertValue { it[0].volume == tickerResponseMock.volume }
                .assertValue { it[0].pair == "BTC/ETH" }
                .assertValue{it[0].baseCurrency ==  "BTC"}
    }

    @Test
    fun getCryptocurrencyDetailsTest(){
        whenever(webSocketService.observePrice(ArgumentMatchers.anyString())).thenReturn(Flowable.just(cryptocurrencyResponse))
        repo.getCryptocurrencyDetails(simpleBtcEthPair)
                .test()
                .assertValue{it.priceFormatted == "${tickerResponseMock.ask} ${simpleBtcEthPair.quoteCurrency}" }
                .assertValue{it.symbol == "BTC/ETH" }
    }

    @Test
    fun noInternetGetCryptocurrencyPairsTest(){
        whenever(checker.isInternetAvailable()).thenReturn(false)
        repo.getCryptocurrencyPairs()
                .test()
                .assertError(NoInternetException::class.java)
    }


}
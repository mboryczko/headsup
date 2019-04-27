package pl.michalboryczko.exercise.viewmodel

import androidx.lifecycle.Observer
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import pl.michalboryczko.exercise.BaseTest
import pl.michalboryczko.exercise.model.CryptocurrencyPair
import pl.michalboryczko.exercise.model.base.Resource
import pl.michalboryczko.exercise.model.exceptions.NoInternetException
import pl.michalboryczko.exercise.source.api.InternetConnectivityChecker
import pl.michalboryczko.exercise.source.repository.Repository
import pl.michalboryczko.exercise.ui.pairs.CryptocurrencyPairViewModel
import junit.framework.Assert.assertEquals
import pl.michalboryczko.exercise.R
import pl.michalboryczko.exercise.source.CryptocurrencyMapper

class CryptocurrencyPairViewModelTest: BaseTest(){


    private val observerState = mock<Observer<Resource<List<CryptocurrencyPair>>>>()
    private val repo = mock(Repository::class.java)
    private val viewmodel by lazy { CryptocurrencyPairViewModel(repo, checker, Schedulers.trampoline(), Schedulers.trampoline()) }



    @Before
    override fun setUp(){
        viewmodel.cryptocurrencyPairs.observeForever(observerState)
        viewmodel.cryptocurrencyPairs.value = Resource.loading()
    }


    @Test
    fun noInternetTest(){
        whenever(repo.getCryptocurrencyPairs()).thenReturn(Single.error(NoInternetException()))
        viewmodel.getCryptocurrencyPairs()

        assertEquals(Resource.error<NoInternetException>(R.string.noInternetConnectionError), viewmodel.cryptocurrencyPairs.value)
    }

    @Test
    fun getCryptocurrencyPairsTest(){
        val mapper = CryptocurrencyMapper()
        val cryptocurrencyPair = mapper.mergeCryptocurrencyPairAndTicker(pairResponseMock, tickerResponseMock)

        whenever(repo.getCryptocurrencyPairs()).thenReturn(Single.just(listOf(cryptocurrencyPair)))
        viewmodel.getCryptocurrencyPairs()

        assertEquals(Resource.success(listOf(cryptocurrencyPair)), viewmodel.cryptocurrencyPairs.value)
    }


}
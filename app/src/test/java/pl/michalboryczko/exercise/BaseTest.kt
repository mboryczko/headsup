package pl.michalboryczko.exercise

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.mockito.Mockito
import org.mockito.stubbing.OngoingStubbing
import pl.michalboryczko.exercise.helper.RandomInputs
import pl.michalboryczko.exercise.model.CryptocurrencyPairDetails
import pl.michalboryczko.exercise.model.CryptocurrencyPairSimple
import pl.michalboryczko.exercise.model.api.CurrencyTickerResponse
import pl.michalboryczko.exercise.model.api.Params
import pl.michalboryczko.exercise.model.api.call.LoginCall
import pl.michalboryczko.exercise.model.api.call.UserCall
import pl.michalboryczko.exercise.source.api.InternetConnectivityChecker

open class BaseTest {
    protected val checker = Mockito.mock(InternetConnectivityChecker::class.java)

    @Before
    open fun setUp(){
        whenever(checker.isInternetAvailableObservable()).thenReturn(Observable.just(true))
    }

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    val random = RandomInputs()

    protected val btcUrl = "https://raw.githubusercontent.com/atomiclabs/cryptocurrency-icons/master/128/color/btc.png"
    protected val ethUrl = "https://raw.githubusercontent.com/atomiclabs/cryptocurrency-icons/master/128/color/eth.png"
    protected val pairDetails = CryptocurrencyPairDetails("BTCETH", "23:26:25", "BTC", "ETH", 0.050043, 0.050043,
            0.050043, "0.050043 ETH", 0.050043)
    protected val pairResponseMock = CurrencyPairResponse("123", "BTC", "ETH", "0.001", "0.001",
            "0.001", "0.001",  "ETH")
    protected val tickerResponseMock = CurrencyTickerResponse(0.050043, 0.050043, 0.050043, 0.050043, 0.050043, 0.050043,
            23525235235.0, 23525235235.0, "2019-02-23T23:26:25.664Z", "BTCETH")

    protected val cryptocurrencyResponse = CryptocurrencyResponse("", "",
            Params(0.050043, 0.050043, 0.050043, 0.050043, 0.050043, 0.050043, 0.050043, 0.050043, "2019-02-23T23:26:25.664Z", "BTCETH"))

    protected val simpleBtcEthPair = CryptocurrencyPairSimple("BTC", "ETH")


    inline fun <reified T> mock() = Mockito.mock(T::class.java)
    inline fun <T> whenever(methodCall: T) : OngoingStubbing<T> =
            Mockito.`when`(methodCall)

    protected fun <T> anyObject(): T {
        return Mockito.anyObject<T>()
    }

    fun generateValidUser(): UserCall {
        val email = random.generateRandomEmail()
        val username = random.generateRandomString(7)
        val password = random.generateStrongPassword()

        return UserCall(email, password, username)
    }

    fun generateValidLoginCall(): LoginCall {
        val email = random.generateRandomEmail()
        val password = random.generateStrongPassword()
        return LoginCall(email, password)
    }
}
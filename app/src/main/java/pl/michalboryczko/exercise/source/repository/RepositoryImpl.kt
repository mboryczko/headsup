package pl.michalboryczko.exercise.source.repository

import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import pl.michalboryczko.exercise.extensions.convertTimestampToPrintableDate
import pl.michalboryczko.exercise.model.CryptocurrencyPairDetails
import pl.michalboryczko.exercise.model.CryptocurrencyPair
import pl.michalboryczko.exercise.model.CryptocurrencyPairSimple
import pl.michalboryczko.exercise.model.api.*
import pl.michalboryczko.exercise.model.api.call.LoginCall
import pl.michalboryczko.exercise.model.api.call.UserCall
import pl.michalboryczko.exercise.model.presentation.User
import pl.michalboryczko.exercise.source.CryptocurrencyMapper
import pl.michalboryczko.exercise.source.UserPreferences
import pl.michalboryczko.exercise.source.api.rest.RestApiService
import pl.michalboryczko.exercise.source.api.InternetConnectivityChecker
import pl.michalboryczko.exercise.source.api.firebase.FirebaseApiService
import pl.michalboryczko.exercise.source.api.firebase.FirestoreApiService
import pl.michalboryczko.exercise.source.api.websocket.WebSocketApiService
import timber.log.Timber
import java.util.concurrent.TimeUnit

class RepositoryImpl(
        private val restApi: RestApiService,
        private val firebaseApiService: FirebaseApiService,
        private val firestoreApiService: FirestoreApiService,
        private val checker: InternetConnectivityChecker
) :Repository, NetworkRepository(checker) {

    override fun saveSession(session: Session): Single<Boolean> {
        return firestoreApiService
                .saveSession(session)
                .compose(handleNetworkConnection())
                .compose(handleExceptions())
    }

    override fun saveStory(story: Story): Single<Boolean> {
        return firestoreApiService
                .saveStory(story)
                .compose(handleNetworkConnection())
                .compose(handleExceptions())
    }

    override fun saveEstimation(estimation: Estimation): Single<Boolean> {
        return firestoreApiService
                .setEstimation(estimation)
                .compose(handleNetworkConnection())
                .compose(handleExceptions())

    }

    /*override fun getCryptocurrencyPairs(): Single<List<CryptocurrencyPair>> {
        return Single.zip(
                    restApi.getCurrencyPairs(),
                    restApi.getCurrencyTickers(),
                    BiFunction{
                        pairsResponse: List<CurrencyPairResponse>, tickerResponse: List<CurrencyTickerResponse> ->
                        val cryptocurrencyPairs = mutableListOf<CryptocurrencyPair>()

                        for (i in pairsResponse.indices){
                            val pair = pairsResponse[i]
                            val ticker = tickerResponse[i]
                            cryptocurrencyPairs.add(mapper.mergeCryptocurrencyPairAndTicker(pair, ticker))
                        }

                        return@BiFunction cryptocurrencyPairs.toList()
                    }
             )
                .compose(handleNetworkConnection())
                .compose(handleExceptions())
    }

    override fun getCryptocurrencyDetails(cryptoPair: CryptocurrencyPairSimple): Flowable<CryptocurrencyPairDetails> {
        return webSocketApiService
                .observePrice(mapper.createPairString(cryptoPair))
                .compose(handleNetworkConnectionFlowable())
                .compose(handleExceptionsFlowable())
                .retry(2)
                .debounce(200, TimeUnit.MILLISECONDS)
                .doOnCancel { webSocketApiService.cancelWebSocket() }
                .map {
                    Timber.d("loglog ${it.params.ask}")
                    Timber.d("loglog ${it.params.symbol} ${it.params.timestamp} ${it.params.volume} " +
                            "${it.params.low} ${it.params.high} ${it.params.ask}")

                        CryptocurrencyPairDetails(
                                mapper.createPairStringSlashSeparated(cryptoPair),
                                it.params.timestamp.convertTimestampToPrintableDate(),
                                mapper.createCryptocurrencyUrl(cryptoPair.baseCurrency),
                                mapper.createCryptocurrencyUrl(cryptoPair.quoteCurrency),
                                it.params.volume, it.params.low, it.params.high,
                                "${it.params.ask} ${cryptoPair.quoteCurrency}", it.params.ask )
                    }
    }*/

}
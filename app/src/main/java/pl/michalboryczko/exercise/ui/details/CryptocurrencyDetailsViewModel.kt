package pl.michalboryczko.exercise.ui.details

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import pl.michalboryczko.exercise.app.BaseViewModel
import pl.michalboryczko.exercise.model.CryptocurrencyPairDetails
import pl.michalboryczko.exercise.model.base.Resource
import pl.michalboryczko.exercise.model.PriceStatus
import pl.michalboryczko.exercise.source.repository.Repository
import timber.log.Timber
import javax.inject.Inject
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import pl.michalboryczko.exercise.R
import pl.michalboryczko.exercise.model.CryptocurrencyPairSimple
import pl.michalboryczko.exercise.model.exceptions.ApiException
import pl.michalboryczko.exercise.model.exceptions.NoInternetException
import pl.michalboryczko.exercise.source.PriceStatusChecker
import pl.michalboryczko.exercise.source.api.InternetConnectivityChecker
import java.lang.Exception
import javax.inject.Named


class CryptocurrencyDetailsViewModel
@Inject constructor(
        private val repository : Repository,
        private val priceStatusChecker: PriceStatusChecker,
        private val connectivityChecker: InternetConnectivityChecker,
        @Named("computationScheduler") private val computationScheduler: Scheduler,
        @Named("mainScheduler") private val mainScheduler: Scheduler

)
    : BaseViewModel(connectivityChecker) {


    val cryptocurrencyPairDetails: MutableLiveData<Resource<CryptocurrencyPairDetails>> = MutableLiveData()
    val priceStatus: MutableLiveData<PriceStatus> = MutableLiveData()
    private var cryptocurrencyDetailsDisposable: Disposable? = null
    private var cachedPairSimple: CryptocurrencyPairSimple? = null

    fun getCryptocurrencyDetails(pairSimple: CryptocurrencyPairSimple){
        /*cryptocurrencyDetailsDisposable =
                repository
                        .getCryptocurrencyDetails(pairSimple)
                        .subscribeOn(computationScheduler)
                        .observeOn(mainScheduler)
                        .doOnSubscribe{
                            if(cryptocurrencyPairDetails.value?.status?.isSuccess() == false)
                                cryptocurrencyPairDetails.value = Resource.loading()
                        }
                        .subscribe(
                                {
                                    priceStatus.value = priceStatusChecker.getPriceStatus(cryptocurrencyPairDetails.value?.data?.price, it.price)
                                    cryptocurrencyPairDetails.value = Resource.success(it)
                                },
                                {
                                    when(it){
                                        is ApiException -> {
                                            cryptocurrencyPairDetails.value = Resource.error(it.errorMsg)
                                            Timber.d("ERROR ${it.errorMsg}")
                                        }

                                        is NoInternetException -> {
                                            cryptocurrencyPairDetails.value = Resource.error(R.string.noInternetConnectionError)
                                            Timber.d("internet connection not available")
                                        }

                                        is Exception -> {
                                            cryptocurrencyPairDetails.value = Resource.error(R.string.unknownError)
                                            Timber.d("${it.localizedMessage}")
                                        }
                                    }
                                }
                        )*/
    }

    fun initViewModel(pairSimple: CryptocurrencyPairSimple){
        cachedPairSimple = pairSimple

    }



    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        val pair = cachedPairSimple
        if(pair != null){
            getCryptocurrencyDetails(pair)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        cryptocurrencyDetailsDisposable?.dispose()
        cryptocurrencyDetailsDisposable = null
    }


    override fun onCleared() {
        super.onCleared()
        cryptocurrencyDetailsDisposable?.dispose()
        cryptocurrencyDetailsDisposable = null
    }
}
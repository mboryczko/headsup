package pl.michalboryczko.exercise.app

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import pl.michalboryczko.exercise.R
import pl.michalboryczko.exercise.model.base.Event
import pl.michalboryczko.exercise.model.exceptions.ApiException
import pl.michalboryczko.exercise.model.exceptions.NoInternetException
import pl.michalboryczko.exercise.model.exceptions.UnathorizedException
import pl.michalboryczko.exercise.model.exceptions.WrongPasswordException
import pl.michalboryczko.exercise.source.remote.InternetConnectivityChecker
import timber.log.Timber

abstract class BaseViewModel(
        internetConnectivityChecker: InternetConnectivityChecker
        ): ViewModel(), LifecycleObserver {

    val toastInfo: MutableLiveData<Event<String>> = MutableLiveData()
    val loggedInStatus: MutableLiveData<Boolean> = MutableLiveData()
    val toastInfoResource: MutableLiveData<Event<Int>> = MutableLiveData()
    protected val disposables :MutableList<Disposable> = mutableListOf()
    val internetConnectivity: MutableLiveData<Boolean> = MutableLiveData()

    init {
        disposables.add(
                internetConnectivityChecker
                        .isInternetAvailableObservable()
                        .subscribe{ internetConnectivity.value = it }
        )
    }


    fun defaultErrorHandling(throwable: Throwable){
        Timber.d("default error: %s", throwable.message)
        Timber.d("default error: %s", throwable.toString())
        when(throwable){
            is NoInternetException -> toastInfoResource.value = Event(R.string.no_internet)
            is UnathorizedException -> {
                toastInfoResource.value = Event(R.string.unathorized_error)
                loggedInStatus.value = false
            }
            is WrongPasswordException -> toastInfoResource.value = Event(R.string.wrong_password)
            is ApiException -> toastInfo.value = Event(throwable.errorMsg)
            else -> toastInfo.value = Event(throwable.localizedMessage)
        }
    }




    override fun onCleared() {
        super.onCleared()
        disposables
                .filter { it.isDisposed }
                .forEach { it.dispose() }
    }


}

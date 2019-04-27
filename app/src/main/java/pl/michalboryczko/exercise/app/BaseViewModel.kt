package pl.michalboryczko.exercise.app

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import pl.michalboryczko.exercise.model.base.Event
import pl.michalboryczko.exercise.source.api.InternetConnectivityChecker

abstract class BaseViewModel(
        internetConnectivityChecker: InternetConnectivityChecker
): ViewModel(), LifecycleObserver {


    val toastInfo: MutableLiveData<Event<String>> = MutableLiveData()
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


    override fun onCleared() {
        super.onCleared()
        disposables
                .filter { it.isDisposed }
                .forEach { it.dispose() }
    }


}

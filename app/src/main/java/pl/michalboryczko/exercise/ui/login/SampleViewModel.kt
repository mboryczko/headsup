package pl.michalboryczko.exercise.ui.login

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.Scheduler
import pl.michalboryczko.exercise.app.BaseViewModel
import pl.michalboryczko.exercise.model.base.Resource
import pl.michalboryczko.exercise.source.remote.InternetConnectivityChecker
import pl.michalboryczko.exercise.source.repository.Repository
import pl.michalboryczko.exercise.utils.Constants.Companion.COMPUTATION_SCHEDULER
import pl.michalboryczko.exercise.utils.Constants.Companion.MAIN_SCHEDULER
import javax.inject.Inject
import javax.inject.Named

class SampleViewModel
@Inject constructor(
        private val repository: Repository,
        private val checker: InternetConnectivityChecker,
        @Named(COMPUTATION_SCHEDULER) private val computationScheduler: Scheduler,
        @Named(MAIN_SCHEDULER) private val mainScheduler: Scheduler
) : BaseViewModel(checker) {


    val status: MutableLiveData<Resource<Int>> = MutableLiveData()

    init {
        disposables.add(
                repository
                        .doSomething()
                        .subscribeOn(computationScheduler)
                        .observeOn(mainScheduler)
                        .doOnSubscribe { status.value = Resource.loading() }
                        .subscribe(
                                {},
                                {defaultErrorHandling(it)}
                        )
        )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        status.value = Resource.initial()
    }


}
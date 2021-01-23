package pl.michalboryczko.exercise.presentation.sample

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.Scheduler
import pl.michalboryczko.exercise.app.BaseViewModel
import pl.michalboryczko.exercise.model.base.Resource
import pl.michalboryczko.exercise.model.base.plusAssign
import pl.michalboryczko.exercise.source.repository.Repository
import pl.michalboryczko.exercise.utils.Constants.Companion.COMPUTATION_SCHEDULER
import pl.michalboryczko.exercise.utils.Constants.Companion.MAIN_SCHEDULER
import javax.inject.Inject
import javax.inject.Named

class SampleViewModel
@Inject constructor(
        private val repository: Repository,
        @Named(COMPUTATION_SCHEDULER) private val computationScheduler: Scheduler,
        @Named(MAIN_SCHEDULER) private val mainScheduler: Scheduler
) : BaseViewModel() {

    val status: MutableLiveData<Resource<Int>> = MutableLiveData()

    init {
        disposables += repository
                        .doSomething()
                        .subscribeOn(computationScheduler)
                        .observeOn(mainScheduler)
                        .doOnSubscribe { status.value = Resource.loading() }
                        .subscribe(
                                {},
                                {
                                    defaultErrorHandling(it)
                                }
                        )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        status.value = Resource.initial()
    }


}
package pl.michalboryczko.exercise.ui.session


import androidx.lifecycle.MutableLiveData
import io.reactivex.Scheduler
import pl.michalboryczko.exercise.R
import pl.michalboryczko.exercise.app.BaseViewModel
import pl.michalboryczko.exercise.model.api.Estimation
import pl.michalboryczko.exercise.model.api.Session
import pl.michalboryczko.exercise.model.api.Story
import pl.michalboryczko.exercise.model.api.call.UserCall
import pl.michalboryczko.exercise.model.base.Event
import pl.michalboryczko.exercise.model.base.Resource
import pl.michalboryczko.exercise.model.exceptions.NoInternetException
import pl.michalboryczko.exercise.model.exceptions.ApiException
import pl.michalboryczko.exercise.source.api.InternetConnectivityChecker
import pl.michalboryczko.exercise.source.repository.Repository
import pl.michalboryczko.exercise.source.repository.UserRepository
import javax.inject.Inject
import timber.log.Timber
import javax.inject.Named


class SessionViewModel
@Inject constructor(
        private val repository: Repository,
        private val checker: InternetConnectivityChecker,
        @Named("computationScheduler") private val computationScheduler: Scheduler,
        @Named("mainScheduler") private val mainScheduler: Scheduler
) : BaseViewModel(checker) {

    val status: MutableLiveData<Resource<Int>> = MutableLiveData()

    init {
        status.value = Resource.initial()
    }

    fun saveSessionClicked(session: Session){
        disposables.add(
                repository
                        .saveSession(session)
                        .subscribeOn(computationScheduler)
                        .observeOn(mainScheduler)
                        .subscribe(
                                {
                                    toastInfo.value = Event("session saved successfully")
                                },
                                {
                                    when(it){
                                        is NoInternetException -> toastInfo.value = Event("no internet access")
                                        is ApiException -> toastInfo.value = Event(it.errorMsg)
                                        else -> toastInfo.value = Event(it.localizedMessage)
                                    }
                                }
                        )
        )
    }

    fun saveStoryClicked(story: Story){
        disposables.add(
                repository
                        .saveStory(story)
                        .subscribeOn(computationScheduler)
                        .observeOn(mainScheduler)
                        .subscribe(
                                {
                                    toastInfo.value = Event("story saved successfully")
                                },
                                {
                                    when(it){
                                        is NoInternetException -> toastInfo.value = Event("no internet access")
                                        is ApiException -> toastInfo.value = Event(it.errorMsg)
                                        else -> toastInfo.value = Event(it.localizedMessage)
                                    }
                                }
                        )
        )
    }

    fun saveEstimationClicked(estimation: Estimation){
        disposables.add(
                repository
                        .saveEstimation(estimation)
                        .subscribeOn(computationScheduler)
                        .observeOn(mainScheduler)
                        .subscribe(
                                {
                                    toastInfo.value = Event("estimation saved successfully")
                                },
                                {
                                    when(it){
                                        is NoInternetException -> toastInfo.value = Event("no internet access")
                                        is ApiException -> toastInfo.value = Event(it.errorMsg)
                                        else -> toastInfo.value = Event(it.localizedMessage)
                                    }
                                }
                        )
        )
    }

}
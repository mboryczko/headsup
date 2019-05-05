package pl.michalboryczko.exercise.ui.session


import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.Scheduler
import pl.michalboryczko.exercise.R
import pl.michalboryczko.exercise.app.BaseViewModel
import pl.michalboryczko.exercise.model.api.Session
import pl.michalboryczko.exercise.model.api.Story
import pl.michalboryczko.exercise.model.base.Event
import pl.michalboryczko.exercise.model.base.Resource
import pl.michalboryczko.exercise.model.exceptions.NoInternetException
import pl.michalboryczko.exercise.model.exceptions.ApiException
import pl.michalboryczko.exercise.model.exceptions.WrongPasswordException
import pl.michalboryczko.exercise.source.api.InternetConnectivityChecker
import pl.michalboryczko.exercise.source.repository.Repository
import pl.michalboryczko.exercise.source.repository.UserRepository
import javax.inject.Inject
import javax.inject.Named


class SessionViewModel
@Inject constructor(
        private val repository: Repository,
        private val userRepository: UserRepository,
        private val checker: InternetConnectivityChecker,
        @Named("computationScheduler") private val computationScheduler: Scheduler,
        @Named("mainScheduler") private val mainScheduler: Scheduler
) : BaseViewModel(checker, userRepository) {

    val session: MutableLiveData<Resource<Session>> = MutableLiveData()
    val master: MutableLiveData<Resource<Session>> = MutableLiveData()

    init {
        session.value = Resource.initial()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        session.value = Resource.initial()
    }

    fun joinSession(sessionId: String, password: String) {
        disposables.add(
                repository
                        .joinSession(sessionId, password)
                        .subscribeOn(computationScheduler)
                        .observeOn(mainScheduler)
                        .doOnSubscribe { session.value = Resource.loading() }
                        .subscribe(
                                {
                                    toastInfo.value = Event("joined session")
                                    session.value = Resource.success(it)
                                },
                                { defaultErrorHandling(it) }
                        )
        )
    }

    fun createSession(name: String, password: String) {
        disposables.add(
                repository
                        .createSession(name, password)
                        .subscribeOn(computationScheduler)
                        .observeOn(mainScheduler)
                        .doOnSubscribe { session.value = Resource.loading() }
                        .subscribe(
                                {
                                    toastInfo.value = Event("session created successfully")
                                    session.value = Resource.success(it)
                                },
                                { defaultErrorHandling(it) }
                        )
        )
    }

    fun logout(){
        disposables.add(
                userRepository
                        .logout()
                        .subscribe(
                                {loggedInStatus.value = false},
                                {}
                        )

        )
    }


}
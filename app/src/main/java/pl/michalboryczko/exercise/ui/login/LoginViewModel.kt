package pl.michalboryczko.exercise.ui.login

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.Scheduler
import pl.michalboryczko.exercise.R
import pl.michalboryczko.exercise.app.BaseViewModel
import pl.michalboryczko.exercise.model.api.call.LoginCall
import pl.michalboryczko.exercise.model.base.Resource
import pl.michalboryczko.exercise.model.exceptions.ApiException
import pl.michalboryczko.exercise.model.exceptions.NoInternetException
import pl.michalboryczko.exercise.source.api.InternetConnectivityChecker
import pl.michalboryczko.exercise.source.repository.UserRepository
import pl.michalboryczko.exercise.ui.register.UserValidator
import pl.michalboryczko.exercise.utils.Constants.Companion.COMPUTATION_SCHEDULER
import pl.michalboryczko.exercise.utils.Constants.Companion.MAIN_SCHEDULER
import javax.inject.Inject
import javax.inject.Named

class LoginViewModel
@Inject constructor(
        private val userRepository: UserRepository,
        private val checker: InternetConnectivityChecker,
        @Named(COMPUTATION_SCHEDULER) private val computationScheduler: Scheduler,
        @Named(MAIN_SCHEDULER) private val mainScheduler: Scheduler
) : BaseViewModel(checker, userRepository) {


    val status: MutableLiveData<Resource<Int>> = MutableLiveData()
    var userValidator: UserValidator

    init {
        userValidator = UserValidator(status)

        disposables.add(
                userRepository
                        .isUserLoggedIn()
                        .subscribeOn(computationScheduler)
                        .observeOn(mainScheduler)
                        .doOnSubscribe { status.value = Resource.loading() }
                        .subscribe(
                                { isLoggedIn ->
                                    if (isLoggedIn) {
                                        status.value = Resource.success(R.string.login_successful)
                                    }else{
                                        status.value = Resource.initial()
                                    }
                                },
                                {}
                        )
        )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        status.value = Resource.initial()
    }

    fun loginClicked(loginInput: LoginCall) {
        if (userValidator.validateLoginInput(loginInput))
            loginUser(loginInput)
    }

    private fun loginUser(loginInput: LoginCall) {
        disposables.add(
                userRepository
                        .logIn(loginInput)
                        .subscribeOn(computationScheduler)
                        .observeOn(mainScheduler)
                        .doOnSubscribe { status.value = Resource.loading() }
                        .subscribe(
                                {
                                    status.value = Resource.success(R.string.login_successful)
                                },
                                {throwable->
                                    when(throwable){
                                        is NoInternetException -> { status.value = Resource.error(R.string.no_internet) }
                                        is ApiException -> status.value = Resource.error(throwable.errorMsg)
                                        else -> status.value = Resource.error(throwable.localizedMessage)
                                    }
                                }
                        )
        )
    }

}
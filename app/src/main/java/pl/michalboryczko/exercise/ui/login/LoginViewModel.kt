package pl.michalboryczko.exercise.ui.login

import androidx.lifecycle.MutableLiveData
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
import javax.inject.Inject
import javax.inject.Named

class LoginViewModel
@Inject constructor(
        private val userRepository: UserRepository,
        private val checker: InternetConnectivityChecker,
        @Named("computationScheduler") private val computationScheduler: Scheduler,
        @Named("mainScheduler") private val mainScheduler: Scheduler
) : BaseViewModel(checker) {


    val status: MutableLiveData<Resource<Int>> = MutableLiveData()
    var userValidator: UserValidator

    init {
        status.value = Resource.initial()
        userValidator = UserValidator(status)
    }

    fun loginClicked(loginInput: LoginCall){
        if(userValidator.validateLoginInput(loginInput))
            loginUser(loginInput)
    }

    private fun loginUser(loginInput: LoginCall){
        disposables.add(
                userRepository
                        .logIn(loginInput)
                        .subscribeOn(computationScheduler)
                        .observeOn(mainScheduler)
                        .doOnSubscribe{ status.value = Resource.loading()}
                        .subscribe(
                                {
                                    status.value = Resource.success(R.string.login_successful)
                                },
                                {
                                    when(it){
                                        is NoInternetException -> status.value = Resource.error(R.string.no_internet)
                                        is ApiException -> status.value = Resource.error(it.errorMsg)
                                        else -> status.value = Resource.error(it.localizedMessage)
                                    }
                                }
                        )
        )
    }



}
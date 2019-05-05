package pl.michalboryczko.exercise.ui.register


import androidx.lifecycle.MutableLiveData
import io.reactivex.Scheduler
import pl.michalboryczko.exercise.R
import pl.michalboryczko.exercise.app.BaseViewModel
import pl.michalboryczko.exercise.model.api.call.UserCall
import pl.michalboryczko.exercise.model.base.Resource
import pl.michalboryczko.exercise.model.exceptions.NoInternetException
import pl.michalboryczko.exercise.model.exceptions.ApiException
import pl.michalboryczko.exercise.source.api.InternetConnectivityChecker
import pl.michalboryczko.exercise.source.repository.UserRepository
import javax.inject.Inject
import timber.log.Timber
import javax.inject.Named


class RegisterViewModel
@Inject constructor(
        private val userRepository: UserRepository,
        private val checker: InternetConnectivityChecker,
        @Named("computationScheduler") private val computationScheduler: Scheduler,
        @Named("mainScheduler") private val mainScheduler: Scheduler
) : BaseViewModel(checker, userRepository) {

    val status: MutableLiveData<Resource<Int>> = MutableLiveData()
    var userValidator: UserValidator

    init {
        status.value = Resource.initial()
        userValidator = UserValidator(status)
    }

    fun registerClicked(user: UserCall){
        if(userValidator.validateUser(user)){
            disposables.add(
                    userRepository
                            .createUser(user)
                            .subscribeOn(computationScheduler)
                            .observeOn(mainScheduler)
                            .doOnSubscribe{ status.value = Resource.loading()}
                            .subscribe(
                                    {
                                        Timber.d("subscribed success")
                                        status.value = Resource.success(R.string.account_created)},
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

}
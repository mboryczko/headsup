package pl.michalboryczko.exercise.source.repository

import io.reactivex.Single
import pl.michalboryczko.exercise.model.api.call.LoginCall
import pl.michalboryczko.exercise.model.api.call.UserCall
import pl.michalboryczko.exercise.source.api.InternetConnectivityChecker
import pl.michalboryczko.exercise.source.api.firebase.FirebaseApiService

class UserRepositoryImpl (
        private var firebaseApiService: FirebaseApiService,
        private val checker: InternetConnectivityChecker
) :UserRepository, NetworkRepository(checker) {


    override fun logIn(input: LoginCall): Single<Boolean> {
        return firebaseApiService.logIn(input)
                .compose(handleExceptions())
    }

    override fun isUserLoggedIn(): Single<Boolean> {
        return firebaseApiService.isUserLoggedIn()
                .compose(handleNetworkConnection())
                .compose(handleExceptions())
    }

    override fun createUser(user: UserCall): Single<Boolean> {
        return firebaseApiService
                .createUser(user)
                .compose(handleNetworkConnection())
                .compose(handleExceptions())
                .map { true }
    }
}
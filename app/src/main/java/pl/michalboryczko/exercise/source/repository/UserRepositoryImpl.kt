package pl.michalboryczko.exercise.source.repository

import io.reactivex.Flowable
import io.reactivex.Single
import pl.michalboryczko.exercise.model.api.call.LoginCall
import pl.michalboryczko.exercise.model.api.call.UserCall
import pl.michalboryczko.exercise.model.presentation.User
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

    override fun logout(): Single<Boolean> {
        return firebaseApiService.logout()
    }

    override fun isUserLoggedIn(): Flowable<Boolean> {
        return firebaseApiService.isUserLoggedIn()
                .compose(handleNetworkConnectionFlowable())
                .compose(handleExceptionsFlowable())
    }

    override fun createUser(user: UserCall): Single<Boolean> {
        return firebaseApiService
                .createUser(user)
                .flatMap {
                    firebaseApiService.addUser(user, it)
                }
                .compose(handleNetworkConnection())
                .compose(handleExceptions())
                .map { true }
    }

    override fun getCurrentUser(): Single<User> {
        return firebaseApiService
                .getCurrentUser()
    }

    override fun getCurrentUserId(): Single<String> {
        return firebaseApiService
            .getCurrentUser()
                .map { it.id }
    }
}
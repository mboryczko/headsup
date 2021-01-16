package pl.michalboryczko.exercise.source.repository

import io.reactivex.Single
import pl.michalboryczko.exercise.source.remote.InternetConnectivityChecker
import pl.michalboryczko.exercise.source.remote.rest.Api

class RepositoryImpl(
        private val api: Api,
        private val checker: InternetConnectivityChecker
) :Repository, NetworkRepository(checker) {

    override fun doSomething(): Single<String> {
        return api.doSomething()
                .compose(handleExceptions())
                .compose(handleNetworkConnection())
    }
}
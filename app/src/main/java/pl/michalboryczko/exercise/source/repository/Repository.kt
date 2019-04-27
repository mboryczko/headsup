package pl.michalboryczko.exercise.source.repository

import io.reactivex.Flowable
import io.reactivex.Single
import pl.michalboryczko.exercise.model.CryptocurrencyPairDetails
import pl.michalboryczko.exercise.model.CryptocurrencyPair
import pl.michalboryczko.exercise.model.CryptocurrencyPairSimple
import pl.michalboryczko.exercise.model.api.Estimation
import pl.michalboryczko.exercise.model.api.Session
import pl.michalboryczko.exercise.model.api.Story
import pl.michalboryczko.exercise.model.api.call.LoginCall
import pl.michalboryczko.exercise.model.api.call.UserCall

interface Repository {
    /*fun getCryptocurrencyPairs() : Single<List<CryptocurrencyPair>>
    fun getCryptocurrencyDetails(cryptoPair: CryptocurrencyPairSimple) : Flowable<CryptocurrencyPairDetails>*/

    fun saveSession(session: Session): Single<Boolean>
    fun saveStory(story: Story): Single<Boolean>
    fun saveEstimation(estimation: Estimation): Single<Boolean>

}

interface UserRepository{
    fun logIn(input: LoginCall): Single<Boolean>
    fun isUserLoggedIn(): Single<Boolean>
    fun createUser(user: UserCall): Single<Boolean>
}
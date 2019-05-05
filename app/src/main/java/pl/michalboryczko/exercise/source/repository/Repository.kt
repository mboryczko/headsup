package pl.michalboryczko.exercise.source.repository

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import pl.michalboryczko.exercise.model.CryptocurrencyPairDetails
import pl.michalboryczko.exercise.model.CryptocurrencyPair
import pl.michalboryczko.exercise.model.CryptocurrencyPairSimple
import pl.michalboryczko.exercise.model.api.Estimation
import pl.michalboryczko.exercise.model.api.Session
import pl.michalboryczko.exercise.model.api.Story
import pl.michalboryczko.exercise.model.api.call.LoginCall
import pl.michalboryczko.exercise.model.api.call.UserCall
import pl.michalboryczko.exercise.model.presentation.User

interface Repository {
    fun createSession(name: String, password: String): Single<Session>
    fun createStory(sessionId: String, story:String, description: String): Single<Story>
    fun saveEstimation(storyId: String, points: Int): Single<Boolean>
    fun observeStories(sessionId: String): Flowable<List<Story>>
    fun observeCurrentStory(sessionId: String): Flowable<Story>
    fun joinSession(sessionId: String, password: String): Single<Session>

}

interface UserRepository{
    fun logIn(input: LoginCall): Single<Boolean>
    fun logout(): Single<Boolean>
    fun isUserLoggedIn(): Flowable<Boolean>
    fun createUser(user: UserCall): Single<Boolean>
    fun getCurrentUser(): Single<User>
    fun getCurrentUserId(): Single<String>
}
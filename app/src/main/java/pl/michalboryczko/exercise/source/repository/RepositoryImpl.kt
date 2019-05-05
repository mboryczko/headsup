package pl.michalboryczko.exercise.source.repository

import io.reactivex.Flowable
import io.reactivex.Single
import pl.michalboryczko.exercise.model.api.*
import pl.michalboryczko.exercise.model.exceptions.NotFoundException
import pl.michalboryczko.exercise.source.api.InternetConnectivityChecker
import pl.michalboryczko.exercise.source.api.firebase.FirebaseApiService
import pl.michalboryczko.exercise.source.api.firebase.FirestoreApiService
import timber.log.Timber

class RepositoryImpl(
        private val userRepository: UserRepository,
        private val firestoreApiService: FirestoreApiService,
        private val checker: InternetConnectivityChecker
) :Repository, NetworkRepository(checker) {

    override fun createSession(name: String, password: String): Single<Session> {
        return userRepository
                .getCurrentUserId()
                .flatMap { managerId ->
                    firestoreApiService.createSession(managerId, name, password)
                }.compose(handleNetworkConnection())
                .compose(handleExceptions())
    }

    /*
        CREATES STORY FOR SPECIFIC SESSION
        AND UPDATES CURRENT_STORY UNDER SPECIFIC SESSION
     */
    override fun createStory(sessionId: String, story:String, description: String): Single<Story> {
        return firestoreApiService
                .saveStory(sessionId, story, description)
                .flatMap {story ->
                    firestoreApiService.updateSession(sessionId, story.storyId)
                            .map { story }
                }
                .compose(handleNetworkConnection())
                .compose(handleExceptions())
    }

    override fun joinSession(sessionId: String, password: String): Single<Session> {
        return firestoreApiService
                .joinSession(sessionId, password)
                .compose(handleNetworkConnection())
                .compose(handleExceptions())
    }

    override fun saveEstimation(storyId: String, points: Int): Single<Boolean> {
        return userRepository
                .getCurrentUser()
                .flatMap { user ->
                    val estimation = Estimation(storyId, points, user.username, user.id)
                    firestoreApiService.setEstimation(estimation)
                }
    }

    override fun observeCurrentStory(sessionId: String): Flowable<Story> {
        return firestoreApiService
                .observeSessionById(sessionId)
                .flatMap { session ->
                    Timber.d("session changed in flatmap %s", session.toString())
                    if(session.currentStory != null){
                        firestoreApiService.loadStory(sessionId, session.currentStory)
                    }else{
                        Flowable.empty()
                    }
                }
                .compose(handleNetworkConnectionFlowable())
                .compose(handleExceptionsFlowable())
    }

    override fun observeStories(sessionId: String): Flowable<List<Story>> {

        return firestoreApiService
                .loadStories(sessionId)
                .compose(handleNetworkConnectionFlowable())
                .compose(handleExceptionsFlowable())
    }

}
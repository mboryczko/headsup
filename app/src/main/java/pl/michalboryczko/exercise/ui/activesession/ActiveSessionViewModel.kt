package pl.michalboryczko.exercise.ui.activesession


import androidx.lifecycle.MutableLiveData
import io.reactivex.Scheduler
import pl.michalboryczko.exercise.app.BaseViewModel
import pl.michalboryczko.exercise.model.ActiveSession
import pl.michalboryczko.exercise.model.api.Session
import pl.michalboryczko.exercise.model.api.Story
import pl.michalboryczko.exercise.model.base.Event
import pl.michalboryczko.exercise.model.base.Resource
import pl.michalboryczko.exercise.model.exceptions.NoInternetException
import pl.michalboryczko.exercise.model.exceptions.ApiException
import pl.michalboryczko.exercise.source.api.InternetConnectivityChecker
import pl.michalboryczko.exercise.source.repository.Repository
import pl.michalboryczko.exercise.source.repository.UserRepository
import javax.inject.Inject
import javax.inject.Named


class ActiveSessionViewModel
@Inject constructor(
        private val repository: Repository,
        private val userRepository: UserRepository,
        private val checker: InternetConnectivityChecker,
        @Named("computationScheduler") private val computationScheduler: Scheduler,
        @Named("mainScheduler") private val mainScheduler: Scheduler
) : BaseViewModel(checker, userRepository) {

    val session: MutableLiveData<Resource<ActiveSession>> = MutableLiveData()
    val story: MutableLiveData<Resource<Story>> = MutableLiveData()


    fun initialize(sessionProvided: Session) {
        disposables.add(
                userRepository
                        .getCurrentUserId()
                        .subscribe(
                                { uid ->
                                    val activeSession = ActiveSession(sessionProvided.managerId == uid, sessionProvided)
                                    session.value = Resource.success(activeSession)
                                    observeCurrentStory(sessionProvided.sessionId)
                                },
                                {defaultErrorHandling(it)}
                        )
        )
    }

    fun observeCurrentStory(sessionId: String){
        disposables.add(
                repository
                        .observeCurrentStory(sessionId)
                        .subscribeOn(computationScheduler)
                        .observeOn(mainScheduler)
                        .doOnSubscribe { story.value = Resource.loading() }
                        .subscribe(
                                {
                                    story.value = Resource.success(it)
                                },
                                {
                                    defaultErrorHandling(it)
                                }
                        )
        )
    }

    fun createStory(story: String, description: String){
        val sessionId = session.value!!.data!!.session.sessionId
        disposables.add(
                repository
                        .createStory(sessionId, story, description)
                        .subscribeOn(computationScheduler)
                        .observeOn(mainScheduler)
                        .subscribe(
                                {
                                    toastInfo.value = Event("story created")
                                },
                                {defaultErrorHandling(it)}
                        )

        )
    }

    fun saveEstimationClicked(points: Int){
        val storyId = story.value?.data?.storyId
        if(storyId != null){
            disposables.add(
                    repository
                            .saveEstimation(storyId, points)
                            .subscribeOn(computationScheduler)
                            .observeOn(mainScheduler)
                            .subscribe(
                                    {
                                        toastInfo.value = Event("estimation saved successfully")
                                    },
                                    {defaultErrorHandling(it)}
                            )
            )
        }

    }


}
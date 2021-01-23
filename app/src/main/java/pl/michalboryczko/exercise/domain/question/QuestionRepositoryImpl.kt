package pl.michalboryczko.exercise.domain.question

import io.reactivex.Single
import pl.michalboryczko.exercise.domain.NetworkRepository
import pl.michalboryczko.exercise.model.models.api.response.Question
import pl.michalboryczko.exercise.model.source.local.room.QuestionDao
import pl.michalboryczko.exercise.model.source.remote.rest.Api

class QuestionRepository(
        private val api: Api,
        private val questionDao: QuestionDao
): NetworkRepository() {

    fun getAllQuestions(): List<Question>{
        val roomQuestions = questionDao.getAll()
        val apiQuestions = api.getQuestions().map { it.questions }

    }

    /*override fun getQuestions(): Single<QuestionResponse> {
        return api.getQuestions()
                .compose(handleExceptions())
                .compose(handleNetworkConnection())
    }*/
}
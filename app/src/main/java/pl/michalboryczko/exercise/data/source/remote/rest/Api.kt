package pl.michalboryczko.exercise.model.source.remote.rest

import io.reactivex.Single
import pl.michalboryczko.exercise.model.models.api.response.QuestionResponse
import retrofit2.http.GET

interface Api {

    @GET("/api/question")
    fun getQuestions() : Single<QuestionResponse>

}
package pl.michalboryczko.exercise.source.remote.rest

import io.reactivex.Single
import retrofit2.http.GET

interface Api {


    @GET("/api/method")
    fun doSomething() : Single<String>

}
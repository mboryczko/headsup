package pl.michalboryczko.exercise.source.repository

import io.reactivex.Single

interface Repository {
    fun doSomething(): Single<String>
}
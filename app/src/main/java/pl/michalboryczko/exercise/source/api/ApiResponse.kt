package pl.michalboryczko.exercise.source.api

/**
 * Created by ${michal_boryczko} on 12.06.2018.
 */
class ApiResponse<T> (
    val error: String,
    val message: String,
    val response: T

)
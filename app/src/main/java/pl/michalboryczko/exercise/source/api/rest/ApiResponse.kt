package pl.michalboryczko.exercise.source.api.rest


class ApiResponse<T> (
    val error: String,
    val message: String,
    val response: T

)
package pl.michalboryczko.exercise.source.remote.rest


class ApiResponse<T> (
    val error: String,
    val message: String,
    val response: T
)
package pl.michalboryczko.exercise.model.api.call

data class UserCall(
        val email: String,
        val password: String,
        val username: String
)
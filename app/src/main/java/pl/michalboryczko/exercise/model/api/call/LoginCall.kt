package pl.michalboryczko.exercise.model.api.call

data class LoginCall(
        val email: String,
        val password: String
)
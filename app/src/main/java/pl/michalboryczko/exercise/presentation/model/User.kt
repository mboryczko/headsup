package pl.michalboryczko.exercise.model.presentation

data class User(
        val id: String,
        val email: String,
        val username: String
){
    constructor(): this("", "", "")
}
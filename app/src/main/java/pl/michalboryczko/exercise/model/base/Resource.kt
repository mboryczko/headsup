package pl.michalboryczko.exercise.model.base


enum class Status {
    SUCCESS,
    ERROR,
    LOADING,
    INITIAL;

    fun isLoading() = this == LOADING
    fun isError() = this == ERROR
}

data class Resource<T>(val status: Status, val data: T? = null, val message: String = "") {

    companion object {

        fun <T> success(data: T): Resource<T> = Resource(Status.SUCCESS, data)
        fun <T> loading(): Resource<T> = Resource(Status.LOADING)
        fun <T> error(message: String): Resource<T> = Resource(Status.ERROR, message = message)
        fun <T> initial(): Resource<T> = Resource(Status.INITIAL)
    }
}
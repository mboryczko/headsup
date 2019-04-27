package pl.michalboryczko.exercise.model.base


enum class Status {
    SUCCESS,
    ERROR_ID,
    ERROR,
    LOADING,
    INITIAL;

    fun isLoading() = this == LOADING
    fun isInitial() = this == INITIAL
    fun isSuccess() = this == SUCCESS
}

data class Resource<T>(val status: Status, val data: T? = null, val message: String = "", val resourceIdMessage: Int = -1) {

    companion object {

        fun <T> success(): Resource<T> = Resource(Status.SUCCESS)
        fun <T> success(data: T): Resource<T> = Resource(Status.SUCCESS, data)
        fun <T> loading(): Resource<T> = Resource(Status.LOADING)
        fun <T> error(message: String): Resource<T> = Resource(Status.ERROR, message = message)
        fun <T> error(resourceIdMessage: Int): Resource<T> = Resource(Status.ERROR_ID, resourceIdMessage = resourceIdMessage)
        fun <T> initial(): Resource<T> = Resource(Status.INITIAL)
    }
}
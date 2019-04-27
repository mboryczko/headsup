package pl.michalboryczko.exercise.source.repository


import io.reactivex.*
import pl.michalboryczko.exercise.model.exceptions.ApiException
import pl.michalboryczko.exercise.model.exceptions.NoInternetException
import pl.michalboryczko.exercise.source.api.InternetConnectivityChecker

open class NetworkRepository(
        private val checker: InternetConnectivityChecker
) {

    protected fun <T> handleNetworkConnection(): SingleTransformer<T, T> = SingleTransformer {
        it.flatMap {
            if(checker.isInternetAvailable()){
                Single.just(it)
            }
            else{
                Single.error(NoInternetException())
            }
        }
    }

    protected fun <T> handleExceptions(): SingleTransformer<T, T> = SingleTransformer {
        it.onErrorResumeNext { t ->
            if(!checker.isInternetAvailable()){
                return@onErrorResumeNext Single.error(NoInternetException())
            }

            return@onErrorResumeNext Single.error(
                    ApiException(t.localizedMessage ?: "Unknown error occurred"))
        }
    }

    protected fun <T> handleNetworkConnectionFlowable(): FlowableTransformer<T, T> = FlowableTransformer {
        it.flatMap {
            if(checker.isInternetAvailable()){
                Flowable.just(it)
            }
            else{
                Flowable.error(NoInternetException())
            }
        }
    }

    protected fun <T> handleExceptionsFlowable(): FlowableTransformer<T, T>{
        return FlowableTransformer {
            it.onErrorResumeNext { t: Throwable ->
                if(!checker.isInternetAvailable()) {
                    return@onErrorResumeNext Flowable.error(NoInternetException())
                }

                return@onErrorResumeNext Flowable.error( ApiException(t.localizedMessage ?: "Unknown error occurred"))
            }
        }
    }

}
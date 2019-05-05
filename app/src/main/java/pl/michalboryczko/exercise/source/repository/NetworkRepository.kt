package pl.michalboryczko.exercise.source.repository


import com.google.firebase.firestore.FirebaseFirestoreException
import io.reactivex.*
import org.intellij.lang.annotations.Flow
import pl.michalboryczko.exercise.model.exceptions.ApiException
import pl.michalboryczko.exercise.model.exceptions.NoInternetException
import pl.michalboryczko.exercise.model.exceptions.UnathorizedException
import pl.michalboryczko.exercise.source.api.InternetConnectivityChecker
import timber.log.Timber

open class NetworkRepository(
        private val checker: InternetConnectivityChecker
) {

    private val PERMISSION_DENIED = "PERMISSION_DENIED"

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

            if (t is FirebaseFirestoreException && t.code.name == PERMISSION_DENIED) {
                Timber.d("unathorized exception cought")
                return@onErrorResumeNext Single.error(UnathorizedException())
            }

            Timber.d("different error cought %s", t.localizedMessage)
            return@onErrorResumeNext Single.error(t)
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

                if (t is FirebaseFirestoreException && t.code.name == PERMISSION_DENIED) {
                    Timber.d("unathorized exception cought")
                    return@onErrorResumeNext Flowable.error(UnathorizedException())
                }


                Timber.d("different error cought %s", t.localizedMessage)
                return@onErrorResumeNext Flowable.error(t)
            }
        }
    }

}
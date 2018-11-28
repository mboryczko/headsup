package pl.michalboryczko.exercise.source.repository

import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pl.michalboryczko.exercise.model.api.ChannelResponse
import pl.michalboryczko.exercise.model.exceptions.ApiException
import pl.michalboryczko.exercise.model.exceptions.NoInternetException
import pl.michalboryczko.exercise.model.exceptions.UnknownException
import pl.michalboryczko.exercise.source.api.ApiService
import pl.michalboryczko.exercise.source.api.InternetConnectivityChecker
import javax.inject.Inject

class ChannelRepositoryImpl
@Inject constructor(
        private val api: ApiService,
        private val checker: InternetConnectivityChecker
): ChannelRepository {

    override fun getChannels(): Single<ChannelResponse> {
        return api.getChannels()
                .compose(handleNetworkConnection())
                .compose(handleExceptions())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    }

    private fun <T> handleNetworkConnection(): SingleTransformer<T, T> = SingleTransformer {
        it.flatMap {
            if(checker.isInternetAvailable()){
                Single.just(it)
            }
            else{
                Single.error(ApiException("No internet connection"))
            }
        }
    }

    private fun <T> handleExceptions(): SingleTransformer<T, T> = SingleTransformer {
        it.onErrorResumeNext { t ->
            if(!checker.isInternetAvailable()){
                return@onErrorResumeNext Single.error(NoInternetException())
            }

            return@onErrorResumeNext Single.error(
                    ApiException(t.localizedMessage ?: "Unknown error occurred"))
        }
    }

}
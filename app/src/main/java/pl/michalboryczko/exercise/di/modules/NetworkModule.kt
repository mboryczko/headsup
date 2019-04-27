package pl.michalboryczko.exercise.di.modules

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pl.michalboryczko.exercise.source.api.rest.Api
import pl.michalboryczko.exercise.source.api.rest.RestApiService
import pl.michalboryczko.exercise.source.api.InternetConnectivityChecker
import pl.michalboryczko.exercise.source.api.firebase.FirebaseApiService
import pl.michalboryczko.exercise.source.api.firebase.FirestoreApiService
import pl.michalboryczko.exercise.source.api.websocket.WebSocketApiService
import java.util.concurrent.TimeUnit


@Module
class NetworkModule {

    val restApiendpoint: String = "https://api.hitbtc.com"
    val webSocketEndpoint = "wss://api.hitbtc.com/api/2/ws"

    @Provides
    fun provideGsonConverter(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    @Provides
    fun provideRxJavaAdapterFactory(): RxJava2CallAdapterFactory {
        return RxJava2CallAdapterFactory.create()
    }


    @Provides
    fun provideInternetConnectivityChecker(context: Context): InternetConnectivityChecker {
        return InternetConnectivityChecker(context)
    }


    @Provides
    fun provideInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    fun provideDefaultOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient().newBuilder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build()

    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, gsonConverterFactory: Converter.Factory, rxJava2CallAdapterFactory: RxJava2CallAdapterFactory): Retrofit {

        return Retrofit.Builder()
                .baseUrl(restApiendpoint)
                .client(okHttpClient)
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .addConverterFactory(gsonConverterFactory)
                .build()
    }

    @Provides
    fun gson(): Gson {
        return Gson()
    }

    @Provides
    fun api(retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }

    @Provides
    fun apiService(context: Context, api: Api): RestApiService {
        return RestApiService(context, api)
    }

    @Provides
    fun firebaseService(): FirebaseApiService {
        return FirebaseApiService()
    }



    @Provides
    fun firestoreService(): FirestoreApiService {
        return FirestoreApiService()
    }

    @Provides
    fun webSocketApiService(okHttpClient: OkHttpClient): WebSocketApiService {
        return WebSocketApiService(webSocketEndpoint, okHttpClient)
    }


}
package pl.michalboryczko.exercise.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pl.michalboryczko.exercise.source.api.Api
import pl.michalboryczko.exercise.source.api.ApiService
import pl.michalboryczko.exercise.source.api.InternetConnectivityChecker
import java.util.concurrent.TimeUnit



@Module
class ApiModule {

    var endpoint: String = "http://api.norbsoft.com"

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
                .baseUrl(endpoint)
                .client(okHttpClient)
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .addConverterFactory(gsonConverterFactory)
                .build()
    }


    @Provides
    fun api(retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }

    @Provides
    fun apiService(context: Context, api: Api): ApiService {
        return ApiService(context, endpoint, api)
    }
}
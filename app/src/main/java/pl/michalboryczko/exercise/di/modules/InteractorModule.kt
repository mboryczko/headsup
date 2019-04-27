package pl.michalboryczko.exercise.di.modules

import dagger.Module
import dagger.Provides
import pl.michalboryczko.exercise.source.CryptocurrencyMapper
import pl.michalboryczko.exercise.source.PriceStatusChecker
import pl.michalboryczko.exercise.source.api.rest.RestApiService
import pl.michalboryczko.exercise.source.api.InternetConnectivityChecker
import pl.michalboryczko.exercise.source.api.firebase.FirebaseApiService
import pl.michalboryczko.exercise.source.api.firebase.FirestoreApiService
import pl.michalboryczko.exercise.source.api.websocket.WebSocketApiService
import pl.michalboryczko.exercise.source.repository.*


@Module
class InteractorModule {

    @Provides
    fun provideCryptocurrencyMapper(): CryptocurrencyMapper {
        return CryptocurrencyMapper()
    }

    @Provides
    fun providePriceStatusChecker(): PriceStatusChecker {
        return PriceStatusChecker()
    }


    @Provides
    fun provideRepository(restApi: RestApiService, firebaseApiService: FirebaseApiService,
                          firestoreApiService: FirestoreApiService,
                          checker: InternetConnectivityChecker): Repository {
        return RepositoryImpl(restApi, firebaseApiService, firestoreApiService, checker)
    }


    @Provides
    fun provideUserRepository(firebaseApiService: FirebaseApiService, checker: InternetConnectivityChecker): UserRepository {
        return UserRepositoryImpl(firebaseApiService, checker)
    }

}
package pl.michalboryczko.exercise.di.modules

import dagger.Module
import dagger.Provides
import pl.michalboryczko.exercise.source.remote.InternetConnectivityChecker
import pl.michalboryczko.exercise.source.remote.rest.Api
import pl.michalboryczko.exercise.source.repository.*


@Module
class InteractorModule {


    @Provides
    fun provideRepository(api: Api, checker: InternetConnectivityChecker): Repository {
        return RepositoryImpl(api, checker)
    }

}
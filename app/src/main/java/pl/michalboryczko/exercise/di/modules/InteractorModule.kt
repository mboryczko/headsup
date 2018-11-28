package pl.michalboryczko.exercise.di.modules

import dagger.Module
import dagger.Provides
import pl.michalboryczko.exercise.source.api.ApiService
import pl.michalboryczko.exercise.source.api.InternetConnectivityChecker
import pl.michalboryczko.exercise.source.repository.ChannelRepository
import pl.michalboryczko.exercise.source.repository.ChannelRepositoryImpl


@Module
class InteractorModule {

    @Provides
    fun provideUserRepository(api: ApiService, checker: InternetConnectivityChecker): ChannelRepository{
        return ChannelRepositoryImpl(api, checker)
    }

}
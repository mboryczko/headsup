package pl.michalboryczko.exercise.di

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import pl.michalboryczko.exercise.di.modules.ApiModule
import pl.michalboryczko.exercise.app.MainApplication
import pl.michalboryczko.exercise.di.modules.InteractorModule
import pl.michalboryczko.exercise.di.modules.MainModule
import javax.inject.Singleton


@Singleton
@Component(
        modules = arrayOf(
                AndroidSupportInjectionModule::class,
                AppModule::class,
                ApiModule::class,
                ViewModelBuilder::class,
                MainModule::class,
                InteractorModule::class
        ))
interface AppComponent : AndroidInjector<MainApplication> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<MainApplication>()
}
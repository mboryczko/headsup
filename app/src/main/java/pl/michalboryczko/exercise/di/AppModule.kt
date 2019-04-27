package pl.michalboryczko.exercise.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pl.michalboryczko.exercise.app.MainApplication
import pl.michalboryczko.exercise.app.Navigator
import pl.michalboryczko.exercise.ui.register.UserValidator
import javax.inject.Named
import javax.inject.Singleton


@Module
class AppModule{

    @Provides
    fun providesContext(application: MainApplication): Context {
        return application.applicationContext
    }


    @Provides
    @Named("computationScheduler")
    fun provideSubscribeScheduler(): Scheduler{
        return Schedulers.computation()
    }

    @Provides
    @Named("mainScheduler")
    fun provideObserveOnScheduler(): Scheduler{
        return AndroidSchedulers.mainThread()
    }

    @Provides
    fun provideNavigator(): Navigator{
        return Navigator()
    }

    /*@Provides
    fun provideUserValidator(): UserValidator{
        return UserValidator()
    }*/

    @Provides
    @Singleton
    internal fun provideSharedPreferences(context: Context): SharedPreferences =
            context.getSharedPreferences("boryczko_exercise", Context.MODE_PRIVATE)


}

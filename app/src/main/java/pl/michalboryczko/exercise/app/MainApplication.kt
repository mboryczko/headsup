package pl.michalboryczko.exercise.app

import android.support.multidex.MultiDex
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import pl.michalboryczko.exercise.di.DaggerAppComponent
import timber.log.Timber


class MainApplication: DaggerApplication() {


    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        Timber.plant(Timber.DebugTree())
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }
}
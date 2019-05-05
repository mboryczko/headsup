package pl.michalboryczko.exercise.app

import androidx.multidex.MultiDex
import com.google.firebase.FirebaseApp
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import leakcanary.LeakCanary
import pl.michalboryczko.exercise.di.DaggerAppComponent
import timber.log.Timber


class MainApplication: DaggerApplication() {


    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        FirebaseApp.initializeApp(this)
        Timber.plant(CustomLoggingTree())
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }


}
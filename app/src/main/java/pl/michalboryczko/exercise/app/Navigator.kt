package pl.michalboryczko.exercise.app

import android.app.Activity
import pl.michalboryczko.exercise.ui.login.SampleActivity
import timber.log.Timber
import javax.inject.Singleton

@Singleton
class Navigator {

    fun navigateToLoginActivity(activity: Activity){
        Timber.d("navigate to login activity called")
        activity.apply { startActivity(SampleActivity.prepareIntent(activity)) }
    }
}
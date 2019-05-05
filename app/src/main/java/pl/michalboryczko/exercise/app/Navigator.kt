package pl.michalboryczko.exercise.app

import android.app.Activity
import pl.michalboryczko.exercise.model.api.Session
import pl.michalboryczko.exercise.ui.activesession.ActiveSessionActivity
import pl.michalboryczko.exercise.ui.session.SessionActivity
import pl.michalboryczko.exercise.ui.login.LoginActivity
import pl.michalboryczko.exercise.ui.register.RegisterActivity
import timber.log.Timber
import javax.inject.Singleton

@Singleton
class Navigator {

    fun navigateToLoginActivity(activity: Activity){
        Timber.d("navigate to login activity called")
        activity.apply { startActivity(LoginActivity.prepareIntent(activity)) }
    }

    fun navigateToRegisterActivity(activity: Activity)
            = activity.apply { startActivity(RegisterActivity.prepareIntent(activity)) }


    fun navigateToActiveSessionActivity(activity: Activity, session: Session)
            = activity.apply { startActivity(ActiveSessionActivity.prepareIntent(activity, session)) }

    fun navigateToCreateSessionActivity(activity: Activity)
            = activity.apply { startActivity(SessionActivity.prepareIntent(activity)) }


    fun navigateToAttendSessionActivity(activity: Activity)
            = activity.apply { startActivity(SessionActivity.prepareIntent(activity)) }
}
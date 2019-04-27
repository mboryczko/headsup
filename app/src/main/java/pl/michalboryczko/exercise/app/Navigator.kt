package pl.michalboryczko.exercise.app

import android.app.Activity
import pl.michalboryczko.exercise.model.CryptocurrencyPairSimple
import pl.michalboryczko.exercise.ui.details.CryptocurrencyDetailsActivity
import pl.michalboryczko.exercise.ui.login.LoginActivity
import pl.michalboryczko.exercise.ui.session.SessionActivity
import javax.inject.Singleton

@Singleton
class Navigator {
    fun navigateToCryptocurrencyDetailsActivity(activity: Activity, pair: CryptocurrencyPairSimple)
            = activity.apply { startActivity(CryptocurrencyDetailsActivity.prepareIntent(activity, pair)) }

    fun navigateToLoginActivity(activity: Activity)
            = activity.apply { startActivity(LoginActivity.prepareIntent(activity)) }


    fun navigateToSessionActivity(activity: Activity)
            = activity.apply { startActivity(SessionActivity.prepareIntent(activity)) }
}
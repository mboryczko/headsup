package pl.michalboryczko.exercise.app

import android.util.Log
import timber.log.Timber

class CustomLoggingTree: Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        Log.d("timber", message)
    }

}
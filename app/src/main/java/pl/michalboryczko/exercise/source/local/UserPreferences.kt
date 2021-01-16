package pl.michalboryczko.exercise.source.local

import android.content.SharedPreferences
import com.google.gson.Gson
import javax.inject.Inject
import android.content.Context

class UserPreferences @Inject constructor(
        private val sharedPreferences: SharedPreferences,
        private val gson: Gson,
        private val context: Context) {

    private object Key {
        const val USER_EMAIL = "USER_EMAIL"
    }

    fun clearPreferences() = sharedPreferences.edit().clear().apply()
}

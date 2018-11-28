package pl.michalboryczko.exercise.source.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class InternetConnectivityChecker
@Inject constructor(val context: Context) {


    fun isInternetAvailable(): Boolean{
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnection = activeNetwork?.isConnectedOrConnecting == true
        Timber.d("internet check $isConnection")
        return isConnection
    }
}
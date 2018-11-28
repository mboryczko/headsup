package pl.michalboryczko.exercise.app

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.Disposable

open abstract class BaseViewModel: ViewModel() {


    val toastInfo: MutableLiveData<String> = MutableLiveData()

    abstract fun onStop()
    abstract fun onResume()


}

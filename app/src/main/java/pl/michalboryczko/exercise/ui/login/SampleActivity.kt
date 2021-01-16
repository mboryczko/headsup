package pl.michalboryczko.exercise.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_login.*
import pl.michalboryczko.exercise.R
import pl.michalboryczko.exercise.app.BaseActivity
import pl.michalboryczko.exercise.model.base.Status

class SampleActivity : BaseActivity<SampleViewModel>() {

    companion object {
        fun prepareIntent(activity: Activity) = Intent(activity, SampleActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel.status.observe(this, Observer {
            it?.let { r ->
                when(r.status){
                    Status.INITIAL -> showInitial()
                    Status.LOADING -> showLoading()
                    Status.ERROR -> showError(r.message)
                    Status.ERROR_ID -> showError(r.resourceIdMessage)
                    Status.SUCCESS -> {  }
                }
            }
        })
    }

    private fun showInitial(){
        showViews()
        hideViews(progressBar)
        enableViews()
    }

    private fun showLoading(){
        showViews(progressBar)
        hideViews()
        disableViews()
    }

    private fun showError(errorMsg: String){
        showViews()
        hideViews(progressBar)
        showToast(this, errorMsg)
        enableViews()
    }

    private fun showError(error: Int){
        showViews()
        hideViews(progressBar)
        showToast(this, error)
        enableViews()
    }

    override fun initViewModel() {
        viewModel = getGenericViewModel()
    }
}

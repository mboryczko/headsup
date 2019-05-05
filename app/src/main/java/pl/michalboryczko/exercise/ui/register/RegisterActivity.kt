package pl.michalboryczko.exercise.ui.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_register.*
import pl.michalboryczko.exercise.R
import pl.michalboryczko.exercise.app.BaseActivity
import pl.michalboryczko.exercise.model.api.call.UserCall
import pl.michalboryczko.exercise.model.base.Status

class RegisterActivity : BaseActivity<RegisterViewModel>() {

    companion object {
        fun prepareIntent(activity: Activity) = Intent(activity, RegisterActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        viewModel.status.observe(this, Observer {
            it?.let { r ->
                when(r.status){
                    Status.INITIAL -> {}
                    Status.LOADING -> showLoading()
                    Status.ERROR -> showError(r.message)
                    Status.ERROR_ID -> showError(r.resourceIdMessage)
                    Status.SUCCESS -> { navigateToLoginActivity() }
                }
            }
        })

        viewModel.internetConnectivity.observe(this, Observer{
            it?.let { isInternetConnection ->
                if(!isInternetConnection) showSnackbar(R.string.no_internet)
            }
        })

        registerButton.setOnClickListener { viewModel.registerClicked(
                UserCall(
                        emailEditText.text.toString(),
                        passwordEditText.text.toString(),
                        usernameEditText.text.toString())
        )}
    }

    private fun navigateToLoginActivity(){
        navigator.navigateToLoginActivity(this)
    }

    private fun showLoading(){
        showViews(progressBar)
        hideViews()
    }

    private fun showError(errorMsg: String){
        showViews()
        hideViews(progressBar)
        showToast(this, errorMsg)
        //permanentErrorTextView.text = errorMsg
    }

    private fun showError(error: Int){
        showViews()
        hideViews(progressBar)

        showSnackbar(error)
    }

    override fun initViewModel() {
        viewModel = getGenericViewModel()
    }
}

package pl.michalboryczko.exercise.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_login.*
import pl.michalboryczko.exercise.R
import pl.michalboryczko.exercise.app.BaseActivity
import pl.michalboryczko.exercise.model.api.call.LoginCall
import pl.michalboryczko.exercise.model.base.Status

class LoginActivity : BaseActivity<LoginViewModel>() {


    companion object {
        fun prepareIntent(activity: Activity) = Intent(activity, LoginActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton.setOnClickListener { viewModel.loginClicked(
                LoginCall(
                        emailEditText.text.toString(),
                        passwordEditText.text.toString())
        )}

        viewModel.status.observe(this, Observer {
            it?.let { r ->
                when(r.status){
                    Status.INITIAL -> {}
                    Status.LOADING -> showLoading()
                    Status.ERROR -> showError(r.message)
                    Status.ERROR_ID -> showError(r.resourceIdMessage)
                    Status.SUCCESS -> { showSnackbar(R.string.login_successful) }
                }
            }
        })
    }

    private fun showLoading(){
        showViews(progressBar)
        hideViews()
    }

    private fun showError(errorMsg: String){
        showViews()
        hideViews(progressBar)
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

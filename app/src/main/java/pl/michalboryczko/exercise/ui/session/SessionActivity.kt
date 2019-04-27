package pl.michalboryczko.exercise.ui.session

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_session.*
import pl.michalboryczko.exercise.R
import pl.michalboryczko.exercise.app.BaseActivity
import pl.michalboryczko.exercise.model.api.Estimation
import pl.michalboryczko.exercise.model.api.Session
import pl.michalboryczko.exercise.model.api.Story

class SessionActivity : BaseActivity<SessionViewModel>() {

    companion object {
        fun prepareIntent(activity: Activity) = Intent(activity, SessionActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session)

        sessionButton.setOnClickListener {
            viewModel.saveSessionClicked(
                    Session(
                            sessionIdEditText.text.toString(),
                            sessionNameEditText.text.toString(),
                            "haslo123" ))
        }

        storyButton.setOnClickListener {
            viewModel.saveStoryClicked(
                    Story(
                            storyIdEditText.text.toString(),
                            sessionIdEditText.text.toString(),
                            storyEditText.text.toString(),
                            storyDescEditText.text.toString(),
                            null))
        }

        estimationButton.setOnClickListener {
            viewModel.saveEstimationClicked(
                    Estimation(
                            storyIdEditText.text.toString(),
                            estimationPointsEditText.text.toString().toInt(),
                            userIdEditText.text.toString()))
        }

    }

    override fun initViewModel() {
        viewModel = getGenericViewModel()
    }
}

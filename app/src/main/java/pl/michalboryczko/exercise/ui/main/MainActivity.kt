package pl.michalboryczko.exercise.ui.main

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import pl.michalboryczko.exercise.R
import pl.michalboryczko.exercise.app.BaseActivity
import pl.michalboryczko.exercise.model.ChannelSimple
import pl.michalboryczko.exercise.model.base.Status

class MainActivity : BaseActivity<MainViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        observeToastMessages()
        observeChannels()

        permanentErrorTextView.setOnClickListener{ viewModel.getChannels() }
    }

    override fun onStop() {
        super.onStop()
        viewModel.onStop()
    }

    override fun onResume(){
        super.onResume()
        viewModel.onResume()
    }

    private fun observeToastMessages(){
        viewModel.toastInfo.observe(this, Observer {
            it?.let{
                showToastMessage(it)
            }
        })
    }

    private fun observeChannels(){
        viewModel.channels.observe(this, Observer {
            it?.let {
                when(it.status){
                    Status.LOADING -> showLoading()
                    Status.ERROR -> showError(it.message)
                    Status.SUCCESS -> {
                        if(it.data != null)
                            showList(it.data)
                    }
                }
            }
        })
    }

    private fun showLoading(){
        progressBar.visibility = View.VISIBLE
        channelsRecycler.visibility = View.GONE
        permanentErrorTextView.visibility = View.GONE
    }

    private fun showList(channels: List<ChannelSimple>){
        progressBar.visibility = View.GONE
        channelsRecycler.visibility = View.VISIBLE
        permanentErrorTextView.visibility = View.GONE

        channelsRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val adapter = ChannelsAdapter(channels)
        channelsRecycler.adapter = adapter
    }

    private fun showError(errorMsg: String){
        progressBar.visibility = View.GONE
        channelsRecycler.visibility = View.GONE
        permanentErrorTextView.visibility = View.VISIBLE
        permanentErrorTextView.text = errorMsg
    }

    override fun initViewModel(){
        viewModel = getGenericViewModel()
    }

}

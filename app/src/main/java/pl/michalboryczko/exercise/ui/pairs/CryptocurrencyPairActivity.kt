package pl.michalboryczko.exercise.ui.pairs

import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_pairs.*
import pl.michalboryczko.exercise.R
import pl.michalboryczko.exercise.app.BaseActivity
import pl.michalboryczko.exercise.model.CryptocurrencyPair
import pl.michalboryczko.exercise.model.base.Status
import pl.michalboryczko.exercise.model.convertToSimple

class CryptocurrencyPairActivity : BaseActivity<CryptocurrencyPairViewModel>(), CryptocurrencyPairsAdapter.OnCryptocurrencyPairClicked {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pairs)

        observeCryptocurrencyPairs()
        permanentErrorTextView.setOnClickListener{ viewModel.getCryptocurrencyPairs() }
    }

    override fun onCryptocurrencyPairClicked(item: CryptocurrencyPair) {
        navigator.navigateToCryptocurrencyDetailsActivity(this, item.convertToSimple())
    }

    private fun observeCryptocurrencyPairs(){
        viewModel.cryptocurrencyPairs.observe(this, Observer {
            it?.let { r ->
                when(r.status){
                    Status.LOADING -> showLoading()
                    Status.ERROR -> showError(r.message)
                    Status.ERROR_ID -> showError(r.resourceIdMessage)
                    Status.SUCCESS -> {
                        if(r.data != null)
                            showList(r.data)
                    }
                }
            }
        })
    }

    private fun showLoading(){
        showViews(progressBar)
        hideViews(headerView, cryptocurrencyPairsRecycler, permanentErrorTextView)
    }

    private fun showList(pairs: List<CryptocurrencyPair>){
        hideViews(progressBar, permanentErrorTextView)
        showViews(headerView, cryptocurrencyPairsRecycler)

        cryptocurrencyPairsRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val adapter = CryptocurrencyPairsAdapter(this, this, pairs)
        cryptocurrencyPairsRecycler.adapter = adapter
    }

    private fun showError(errorMsg: Int){
        hideViews(progressBar, cryptocurrencyPairsRecycler)
        showViews(permanentErrorTextView)
        permanentErrorTextView.text = getString(errorMsg)
    }

    private fun showError(errorMsg: String){
        hideViews(progressBar, cryptocurrencyPairsRecycler)
        showViews(permanentErrorTextView)
        permanentErrorTextView.text = errorMsg
    }

    override fun initViewModel(){
        viewModel = getGenericViewModel()
    }

}

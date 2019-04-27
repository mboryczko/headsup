package pl.michalboryczko.exercise.ui.details

import android.app.Activity
import androidx.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_cryptocurrency_details.*
import pl.michalboryczko.exercise.app.BaseActivity
import pl.michalboryczko.exercise.R
import pl.michalboryczko.exercise.model.CryptocurrencyPairDetails
import pl.michalboryczko.exercise.model.CryptocurrencyPairSimple
import pl.michalboryczko.exercise.model.PriceStatus
import pl.michalboryczko.exercise.model.base.Status
import pl.michalboryczko.exercise.utils.Constants


class CryptocurrencyDetailsActivity : BaseActivity<CryptocurrencyDetailsViewModel>() {

    companion object {
        fun prepareIntent(activity: Activity, pair: CryptocurrencyPairSimple) = Intent(activity, CryptocurrencyDetailsActivity::class.java)
                .putExtra(Constants.CRYPTOCURRENCY_DETAILS_EXTRA_BASE, pair.baseCurrency)
                .putExtra(Constants.CRYPTOCURRENCY_DETAILS_EXTRA_QUOTE, pair.quoteCurrency)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cryptocurrency_details)

        initViewModelData()
        observeCryptocurrency()
        observePriceStatus()
        permanentErrorTextView.setOnClickListener{ retryFetchingData() }
    }

    private fun retryFetchingData(){
        viewModel.getCryptocurrencyDetails(unpackExtras())
    }

    private fun initViewModelData(){
        viewModel.initViewModel(unpackExtras())
    }

    private fun unpackExtras(): CryptocurrencyPairSimple{
        return CryptocurrencyPairSimple(intent.getStringExtra(Constants.CRYPTOCURRENCY_DETAILS_EXTRA_BASE),
                intent.getStringExtra(Constants.CRYPTOCURRENCY_DETAILS_EXTRA_QUOTE) )
    }

    private fun observePriceStatus(){
        viewModel.priceStatus.observe(this, Observer {
            it?.let {
                val priceIndicatorDrawable = when(it){
                    PriceStatus.HIGHER -> R.drawable.ic_arrow_drop_up_green_24dp
                    PriceStatus.LOWER ->  R.drawable.ic_arrow_drop_down_red_24dp
                    PriceStatus.SAME ->  R.drawable.ic_same_white_24dp
                }
                priceIndicator.setImageDrawable(ContextCompat.getDrawable(this, priceIndicatorDrawable))
            }
        })
    }

    private fun observeCryptocurrency(){
        viewModel.cryptocurrencyPairDetails.observe(this, Observer {
            it?.let { r ->
                when(r.status){
                    Status.INITIAL -> {}
                    Status.LOADING -> showLoading()
                    Status.ERROR -> showError(r.message)
                    Status.ERROR_ID -> showError(r.resourceIdMessage)
                    Status.SUCCESS -> {
                        if(r.data != null)
                            showCryptocurrency(r.data)
                    }
                }
            }
        })
    }

    private fun showLoading(){
        showViews(progressBar)
        hideViews(symbolTextView, priceTextView, timeDescriptionItemView, volumeDescriptionItemView,
                lowDescriptionItemView, highDescriptionItemView, permanentErrorTextView)
    }

    private fun showCryptocurrency(pair: CryptocurrencyPairDetails){
        showViews(symbolTextView, priceTextView, timeDescriptionItemView, volumeDescriptionItemView,
                lowDescriptionItemView, highDescriptionItemView)
        hideViews(progressBar, permanentErrorTextView)
        symbolTextView.text = pair.symbol
        priceTextView.text = pair.priceFormatted
        timeDescriptionItemView.setDescriptionTextView(pair.timeUpdated)
        volumeDescriptionItemView.setDescriptionTextView(pair.volume.toString())
        lowDescriptionItemView.setDescriptionTextView(pair.low.toString())
        highDescriptionItemView.setDescriptionTextView(pair.high.toString())

        Glide.with(this)
                .load(pair.baseCurrencyUrl)
                .into(baseCurrencyImage)

        Glide.with(this)
                .load(pair.quoteCurrencyUrl)
                .into(quoteCurrencyImage)

    }

    private fun showError(errorMsg: String){
        showViews(permanentErrorTextView)
        hideViews(symbolTextView, priceTextView, timeDescriptionItemView, volumeDescriptionItemView,
            lowDescriptionItemView, highDescriptionItemView)

        permanentErrorTextView.text = errorMsg
    }

    private fun showError(error: Int){
        showViews(permanentErrorTextView)
        hideViews(progressBar, priceTextView, timeDescriptionItemView, volumeDescriptionItemView,
                lowDescriptionItemView, highDescriptionItemView)

        permanentErrorTextView.text = getString(error)
    }

    override fun initViewModel(){
        viewModel = getGenericViewModel()
    }
}

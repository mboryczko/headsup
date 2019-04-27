package pl.michalboryczko.exercise.ui.pairs

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import pl.michalboryczko.exercise.R
import kotlinx.android.synthetic.main.cryptocurrency_pair_item.view.*
import pl.michalboryczko.exercise.model.CryptocurrencyPair

class CryptocurrencyPairsAdapter(
        private val context: Context,
        private val onCryptocurrencyPairClicked: OnCryptocurrencyPairClicked,
        private val list: List<CryptocurrencyPair>) : RecyclerView.Adapter<CryptocurrencyPairsAdapter.ViewHolder>(){

    interface OnCryptocurrencyPairClicked{
        fun onCryptocurrencyPairClicked(item: CryptocurrencyPair)
    }

    private val colorPositionEven = ContextCompat.getColor(context, R.color.color4 )
    private val colorPositionOdd = ContextCompat.getColor(context, R.color.color3 )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cryptocurrency_pair_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        with(holder){
            val backgroundColor = if(position % 2 == 0) colorPositionEven else colorPositionOdd
            holder.wholeRow.setBackgroundColor(backgroundColor)

            pair.text = item.pair
            lastPrice.text = item.lastPrice.toString()
            volume.text = item.volume.toString()
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val pair: TextView = itemView.pairTextView
        val lastPrice: TextView  = itemView.lastPriceTextView
        val volume: TextView  = itemView.volumeTextView
        val wholeRow: View  = itemView.wholeRow

        init {
            wholeRow.setOnClickListener {
                val item = list[layoutPosition]
                onCryptocurrencyPairClicked.onCryptocurrencyPairClicked(item)
            }
        }

    }
}
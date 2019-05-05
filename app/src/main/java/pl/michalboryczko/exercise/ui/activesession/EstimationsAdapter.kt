package pl.michalboryczko.exercise.ui.activesession

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.estimation_item.view.*
import pl.michalboryczko.exercise.R
import pl.michalboryczko.exercise.model.api.Estimation

class EstimationsAdapter(val items : List<Estimation>, val context: Context) : RecyclerView.Adapter<EstimationsAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.estimation_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        with(item){
            holder.pointsEstimation.text = points.toString()
            holder.usernameEstimation.text = username
        }

    }


    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val pointsEstimation = view.pointsEstimationItem
        val usernameEstimation = view.usernameEstimationItem
    }
}
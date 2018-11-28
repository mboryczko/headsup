package pl.michalboryczko.exercise.ui.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import pl.michalboryczko.exercise.R
import kotlinx.android.synthetic.main.channel_item.view.*
import pl.michalboryczko.exercise.model.ChannelSimple

class ChannelsAdapter(private val list: List<ChannelSimple>) : RecyclerView.Adapter<ChannelsAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.channel_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        with(holder){
            title.text = item.title
            description.text = item.description
            Glide.with(itemView.context)
                    .load(item.image)
                    .into(image)
        }


    }

    inner class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.title
        val description: TextView  = itemView.description
        val image: ImageView  = itemView.image

    }
}
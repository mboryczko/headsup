package pl.michalboryczko.exercise.ui.details

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.description_item_view.view.*
import pl.michalboryczko.exercise.R


class DescriptionItemView(context: Context, attrs: AttributeSet): ConstraintLayout(context, attrs) {

    init {
        inflate(context, R.layout.description_item_view, this)

        val descriptionImageView: ImageView = findViewById(R.id.descriptionImageView)
        val descriptionInfoTextView: TextView = findViewById(R.id.descriptionInfoTextView)
        val descriptionTextView: TextView = findViewById(R.id.descriptionTextView)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.DescriptionItemView)
        descriptionImageView.setImageDrawable(attributes.getDrawable(R.styleable.DescriptionItemView_descriptionImageView))
        descriptionInfoTextView.text = attributes.getString(R.styleable.DescriptionItemView_descriptionInfoTextView)
        descriptionTextView.text = attributes.getString(R.styleable.DescriptionItemView_descriptionTextView)
        attributes.recycle()
    }

    fun setDescriptionTextView(text: String){
        descriptionTextView.text = text
    }
}
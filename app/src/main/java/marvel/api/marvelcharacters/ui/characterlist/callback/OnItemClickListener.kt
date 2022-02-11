package marvel.api.marvelcharacters.ui.characterlist.callback

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import marvel.api.marvelcharacters.data.model.Results

interface OnItemClickListener {
    fun onItemClick(result: Results, imageView: ImageView)
}
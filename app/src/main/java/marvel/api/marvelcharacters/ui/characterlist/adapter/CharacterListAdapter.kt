package marvel.api.marvelcharacters.ui.characterlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import marvel.api.marvelcharacters.data.model.Results
import marvel.api.marvelcharacters.databinding.ItemCharacterBinding
import marvel.api.marvelcharacters.ui.characterlist.callback.OnItemClickListener
import com.bumptech.glide.load.model.LazyHeaders

import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.engine.DiskCacheStrategy

import com.bumptech.glide.request.RequestOptions







/**
 * Created by Jeeban Bagdi on 1/31/2022.
 */
class CharacterListAdapter(private val onItemClickListener: OnItemClickListener): PagingDataAdapter<Results, CharacterListAdapter.CharcterViewHolder>(diffCallback = CHARACTER_COMPARATOR) {

    class CharcterViewHolder(val binding: ItemCharacterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(results: Results?) {
            if (null != results){
                showData(results)
            }else{
                binding.tvCharacterName.text = "Loading..."
            }
        }

        private fun showData(results: Results) {
            Glide.with(binding.root.context)
                .load("${results.thumbnail?.path}.${results.thumbnail?.extension}")
                .apply(RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL))
                .centerCrop()
                .into(binding.ivCharacter)
            binding.tvCharacterName.text = results.name

        }
    }



    override fun onBindViewHolder(holder: CharcterViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.binding.cvItem.setOnClickListener {
            onItemClickListener.onItemClick(getItem(position)!!)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharcterViewHolder {
       return CharcterViewHolder(ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    companion object {
        private val CHARACTER_COMPARATOR = object : DiffUtil.ItemCallback<Results>() {
            override fun areItemsTheSame(oldItem: Results, newItem: Results): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Results, newItem: Results): Boolean =
                oldItem == newItem
        }
    }


}
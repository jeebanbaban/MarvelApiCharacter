package marvel.api.marvelcharacters.ui.characterlist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import marvel.api.marvelcharacters.databinding.LoadingListItemBinding

/**
 * Created by Jeeban Bagdi on 1/31/2022.
 */
class FooterAdapter(private val retry: () -> Unit): LoadStateAdapter<FooterAdapter.FooterViewHolder>() {

    class FooterViewHolder(private val retry: () -> Unit, private val binding: LoadingListItemBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.retryButton.setOnClickListener {
                retry.invoke()
            }
        }
        fun bind(loadState: LoadState){
            if (loadState is LoadState.Error) {
                binding.errorMsg.text = loadState.error.localizedMessage
            }
            binding.errorMsg.isVisible = loadState !is LoadState.Loading
            binding.loadingItemProgressBar.isVisible = loadState is LoadState.Loading
            binding.retryButton.isVisible = loadState !is LoadState.Loading

        }
    }

    override fun onBindViewHolder(holder: FooterViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): FooterViewHolder {
        return FooterViewHolder(retry, LoadingListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
}
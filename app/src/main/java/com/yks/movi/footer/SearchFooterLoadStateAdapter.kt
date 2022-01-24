package com.yks.movi.footer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yks.movi.databinding.SearchFooterLoadStateItemBinding

class SearchFooterLoadStateAdapter(private val retry: () -> Unit): LoadStateAdapter<SearchFooterLoadStateAdapter.ViewHolder>() {

    class ViewHolder (val binding: SearchFooterLoadStateItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        return ViewHolder(
            SearchFooterLoadStateItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.binding.apply {
            retryImg.setOnClickListener { retry.invoke() }
            retryImg.isVisible = loadState !is LoadState.Loading
            circularProgress.isVisible = loadState is LoadState.Loading
        }
    }

}
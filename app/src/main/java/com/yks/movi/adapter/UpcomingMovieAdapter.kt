package com.yks.movi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yks.movi.R
import com.yks.movi.databinding.MovieItemBinding
import com.yks.movi.model.MovieResult
import com.yks.movi.utils.Credentials
import com.yks.movi.utils.downloadFromUrl
import com.yks.movi.view.MainFragmentDirections

class UpcomingMovieAdapter: PagingDataAdapter<MovieResult,UpcomingMovieAdapter.ViewHolder>(
    UpcomingMovieDiffCallBack) {

    class ViewHolder(val binding: MovieItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            MovieItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.titleTxt.text = getItem(position)?.title
        holder.binding.voteAverageTxt.text = getItem(position)?.voteAverage.toString()

        getItem(position)?.posterPath?.let {
            holder.binding.movieImg.downloadFromUrl(Credentials.BASE_URL_TO_IMAGE.plus(it),holder.itemView.context)
        }?:holder.binding.movieImg.setImageResource(R.drawable.error)

        holder.itemView.setOnClickListener {view ->
            getItem(position)?.id?.let {
                val action = MainFragmentDirections.actionMainFragmentToDetailsFragment(it)
                view.findNavController().navigate(action)
            }
        }
    }

    companion object {
        private val UpcomingMovieDiffCallBack = object : DiffUtil.ItemCallback<MovieResult>(){
            override fun areItemsTheSame(oldItem: MovieResult, newItem: MovieResult): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MovieResult, newItem: MovieResult): Boolean {
                return oldItem == newItem
            }
        }
    }

}
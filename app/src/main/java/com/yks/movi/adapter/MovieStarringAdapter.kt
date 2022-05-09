package com.yks.movi.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yks.movi.R
import com.yks.movi.databinding.MovieItemBinding
import com.yks.movi.model.MovieResult
import com.yks.movi.utils.Credentials
import com.yks.movi.utils.downloadFromUrl

class MovieStarringAdapter: RecyclerView.Adapter<MovieStarringAdapter.ViewHolder>() {

    private val actorMovieList: ArrayList<MovieResult> = arrayListOf()

    class ViewHolder(val binding: MovieItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            MovieItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movies = actorMovieList[position]
        holder.binding.titleTxt.text = movies.title
        holder.binding.voteAverageTxt.text = movies.voteAverage.toString()

        movies.posterPath?.let {
            holder.binding.movieImg.downloadFromUrl(Credentials.BASE_URL_TO_IMAGE.plus(it),holder.itemView.context)
        }?: holder.binding.movieImg.setImageResource(R.drawable.error)

        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it(movies)
            }
        }
    }

    override fun getItemCount(): Int {
        return actorMovieList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<MovieResult>){
        actorMovieList.clear()
        actorMovieList.addAll(list)
        notifyDataSetChanged()
    }

    private var onItemClickListener: ((MovieResult) -> Unit)? = null

    fun setOnItemClickListener(listener: (MovieResult) -> Unit) { onItemClickListener = listener }

}
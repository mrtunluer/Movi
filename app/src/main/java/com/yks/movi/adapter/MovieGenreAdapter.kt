package com.yks.movi.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yks.movi.databinding.GenreItemBinding
import com.yks.movi.model.Genre

class MovieGenreAdapter: RecyclerView.Adapter<MovieGenreAdapter.ViewHolder>() {

    private val genreList: ArrayList<Genre> = arrayListOf()

    class ViewHolder(val binding: GenreItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            GenreItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val genre = genreList[position]
        viewHolder.binding.genreTxt.text = genre.name
    }

    override fun getItemCount(): Int {
        return genreList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<Genre>){
        genreList.clear()
        genreList.addAll(list)
        notifyDataSetChanged()
    }


}
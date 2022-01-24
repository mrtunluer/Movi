package com.yks.movi.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.yks.movi.R
import com.yks.movi.databinding.CastItemBinding
import com.yks.movi.model.Cast
import com.yks.movi.utils.Credentials
import com.yks.movi.utils.downloadFromUrl
import com.yks.movi.view.DetailsFragmentDirections

class CastAdapter: RecyclerView.Adapter<CastAdapter.ViewHolder>(){
    private val castList: ArrayList<Cast> = arrayListOf()

    class ViewHolder(val binding: CastItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CastItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val cast = castList[position]
        cast.profilePath?.let {
            viewHolder.binding.castImg.downloadFromUrl(Credentials.BASE_URL_TO_PROFILE_IMAGE.plus(cast.profilePath),viewHolder.itemView.context)
        }?: viewHolder.binding.castImg.setImageResource(R.drawable.profile)
        viewHolder.binding.castName.text = cast.originalName

        viewHolder.itemView.setOnClickListener {view ->
            cast.id?.let {
                val action = DetailsFragmentDirections.actionDetailsFragmentToActorFragment(it)
                view.findNavController().navigate(action)
            }
        }
    }

    override fun getItemCount(): Int {
        return castList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<Cast>){
        castList.clear()
        castList.addAll(list)
        notifyDataSetChanged()
    }



}
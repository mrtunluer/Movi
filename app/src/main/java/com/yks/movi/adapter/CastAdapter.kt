package com.yks.movi.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yks.movi.R
import com.yks.movi.databinding.CastItemBinding
import com.yks.movi.model.Cast
import com.yks.movi.utils.Credentials
import com.yks.movi.utils.downloadFromUrl

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

        viewHolder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it(cast)
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

    private var onItemClickListener: ((Cast) -> Unit)? = null

    fun setOnItemClickListener(listener: (Cast) -> Unit) { onItemClickListener = listener }


}
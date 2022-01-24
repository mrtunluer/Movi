package com.yks.movi.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yks.movi.R
import com.yks.movi.databinding.ProviderItemBinding
import com.yks.movi.model.FlatRate
import com.yks.movi.utils.Credentials
import com.yks.movi.utils.downloadFromUrl

class ProviderAdapter: RecyclerView.Adapter<ProviderAdapter.ViewHolder>() {
    private val flatRateList: ArrayList<FlatRate> = arrayListOf()

    class ViewHolder(val binding: ProviderItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ProviderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val provider = flatRateList[position]
        holder.binding.providerName.text = provider.providerName
        provider.logoPath?.let {
            holder.binding.providerLogo.downloadFromUrl(Credentials.ORG_BASE_URL_TO_IMAGE.plus(it),holder.itemView.context)
        }
    }

    override fun getItemCount(): Int {
        return flatRateList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<FlatRate>){
        flatRateList.clear()
        flatRateList.addAll(list)
        notifyDataSetChanged()
    }

}
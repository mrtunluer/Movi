package com.yks.movi.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.yks.movi.R

fun ImageView.downloadFromUrl(url:String?,context: Context){
    Glide.with(context)
        .load(url)
        .placeholder(R.drawable.placeholder)
        .error(R.drawable.error)
        .into(this)
}
package com.yks.movi.model

import com.google.gson.annotations.SerializedName

data class Casts(
    @SerializedName("cast")
    val cast: ArrayList<Cast>?
)

data class Cast(
    @SerializedName("id")
    val id: Long?,
    @SerializedName("original_name")
    val originalName: String?,
    @SerializedName("profile_path")
    val profilePath: String?
)

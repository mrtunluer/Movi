package com.yks.movi.model

import com.google.gson.annotations.SerializedName

data class ActorProfile(
    @SerializedName("biography")
    val biography: String?,
    @SerializedName("birthday")
    val birthday: String?,
    @SerializedName("deathday")
    val deathDay: String?,
    @SerializedName("id")
    val id: Long?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("place_of_birth")
    val placeOfBirth: String?,
    @SerializedName("profile_path")
    val profilePath: String?,
    @SerializedName("credits")
    val credits: Credits?
    )

data class Credits( //starring movies
    @SerializedName("cast")
    val cast: ArrayList<MovieResult>?
    )


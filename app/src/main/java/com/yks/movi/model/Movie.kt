package com.yks.movi.model

import com.google.gson.annotations.SerializedName


data class Movie (
    @SerializedName("page")
    val page: Int?,
    @SerializedName("results")
    val movieResults: ArrayList<MovieResult>?,
    @SerializedName("total_pages")
    val totalPages: Int?,
    @SerializedName("total_results")
    val totalResults: Int?
)
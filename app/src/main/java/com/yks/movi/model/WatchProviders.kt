package com.yks.movi.model

import com.google.gson.annotations.SerializedName


data class WatchProviders(
    @SerializedName("results")
    val results: ProviderResults?
    )

data class ProviderResults(
    @SerializedName("TR")
    val tr: TrProviderResults?
    )

data class TrProviderResults(
    @SerializedName("flatrate")
    val flatRate: ArrayList<FlatRate>?,
    @SerializedName("link")
    val link: String?
    )

data class FlatRate(
    @SerializedName("display_priority")
    val displayPriority: Int?,
    @SerializedName("logo_path")
    val logoPath: String?,
    @SerializedName("provider_name")
    val providerName: String?,
    @SerializedName("provider_id")
    val providerId: Int?
    )
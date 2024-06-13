package com.altunoymak.esarj.data.model.searchstation

import com.google.gson.annotations.SerializedName

data class SearchChargingStation(
    @SerializedName("address")
    val address: String?,
    @SerializedName("city")
    val city: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("location")
    val location: Location?,
    @SerializedName("plugs")
    val plugs: List<Plug?>?,
    @SerializedName("plugsTotal")
    val plugsTotal: Int?,
    @SerializedName("pointOfInterests")
    val pointOfInterests: List<Any?>?,
    @SerializedName("provideLiveStats")
    val provideLiveStats: Boolean?,
    @SerializedName("provider")
    val provider: String?,
    @SerializedName("title")
    val title: String?
)
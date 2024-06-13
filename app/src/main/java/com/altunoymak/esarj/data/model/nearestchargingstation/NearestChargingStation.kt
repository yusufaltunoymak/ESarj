package com.altunoymak.esarj.data.model.nearestchargingstation

import com.google.gson.annotations.SerializedName

data class NearestChargingStation(
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
    val pointOfInterests: List<String?>?,
    @SerializedName("provideLiveStats")
    val provideLiveStats: Boolean?,
    @SerializedName("provider")
    val provider: String?,
    @SerializedName("title")
    val title: String?,
    var distance: Double = 0.0
)
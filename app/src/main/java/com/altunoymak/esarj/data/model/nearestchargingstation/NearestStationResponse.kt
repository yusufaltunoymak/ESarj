package com.altunoymak.esarj.data.model.nearestchargingstation

import com.google.gson.annotations.SerializedName

data class NearestStationResponse(
    @SerializedName("chargingStations")
    val nearestChargingStations: List<NearestChargingStation?>?,
    @SerializedName("distance")
    val distance: Int?,
    @SerializedName("distanceUnit")
    val distanceUnit: String?,
    @SerializedName("total")
    val total: Int?
)
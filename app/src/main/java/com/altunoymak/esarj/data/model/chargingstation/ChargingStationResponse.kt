package com.altunoymak.esarj.data.model.chargingstation

import com.google.gson.annotations.SerializedName

data class ChargingStationResponse(
    @SerializedName("chargingStations")
    val chargingStations: List<ChargingStation?>?
)
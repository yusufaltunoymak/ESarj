package com.altunoymak.esarj.data.model.chargingstation

import com.google.gson.annotations.SerializedName

data class ChargingStation(
    @SerializedName("id")
    val id: String?,
    @SerializedName("location")
    val location: Location?,
    @SerializedName("provider")
    val provider: String?
)
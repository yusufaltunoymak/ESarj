package com.altunoymak.esarj.data.model.nearestchargingstation

import com.google.gson.annotations.SerializedName

data class Plug(
    @SerializedName("count")
    val count: Int?,
    @SerializedName("power")
    val power: String?,
    @SerializedName("type")
    val type: String?
)
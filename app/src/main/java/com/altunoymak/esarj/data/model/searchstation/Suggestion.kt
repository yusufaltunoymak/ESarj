package com.altunoymak.esarj.data.model.searchstation

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Suggestion(
    @SerializedName("chargingStation")
    val chargingStation: SearchChargingStation?,
    @SerializedName("highlightedText")
    val highlightedText: String?
) : Serializable
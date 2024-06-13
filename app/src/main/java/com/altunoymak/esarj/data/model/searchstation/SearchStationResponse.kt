package com.altunoymak.esarj.data.model.searchstation

import com.google.gson.annotations.SerializedName

data class SearchStationResponse(
    @SerializedName("suggestions")
    val suggestions: List<Suggestion?>?,
    @SerializedName("total")
    val total: Int?
)
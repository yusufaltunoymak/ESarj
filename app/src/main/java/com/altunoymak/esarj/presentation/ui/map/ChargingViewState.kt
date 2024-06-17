package com.altunoymak.esarj.presentation.ui.map

import com.altunoymak.esarj.data.model.chargingstation.ChargingStation
import com.altunoymak.esarj.data.model.chargingstationdetail.DetailStation
import com.altunoymak.esarj.data.model.nearestchargingstation.NearestChargingStation

data class ChargingViewState(
    val isLoading: Boolean? = null,
    val chargingList: List<ChargingStation?>? = emptyList(),
    val chargingDetail : DetailStation? = null,
    val nearestList : List<NearestChargingStation?>?= null,
    val errorMessage: String? = null
)
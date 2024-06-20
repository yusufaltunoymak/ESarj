package com.altunoymak.esarj.util

import com.altunoymak.esarj.data.local.FavoriteStationEntity
import com.altunoymak.esarj.data.model.chargingstationdetail.DetailStation

fun DetailStation.toFavoriteStationEntity(): FavoriteStationEntity {
    return FavoriteStationEntity(
        id = this.id ?: "",
        address = this.address ?: "",
        lat = this.location?.lat ?: 0.0,
        lon = this.location?.lon ?: 0.0,
        title = this.title ?: "",
    )
}
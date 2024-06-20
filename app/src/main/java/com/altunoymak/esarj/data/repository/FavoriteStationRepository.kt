package com.altunoymak.esarj.data.repository

import com.altunoymak.esarj.data.local.FavoriteStationDao
import com.altunoymak.esarj.data.local.FavoriteStationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteStationRepository @Inject constructor(private val dao: FavoriteStationDao) {
    suspend fun addFavoriteStation(station: FavoriteStationEntity) = dao.addFavoriteStation(station)
    suspend fun removeFavoriteStation(station: FavoriteStationEntity) = dao.removeFavoriteStation(station)
    fun getFavoriteStations(): Flow<List<FavoriteStationEntity>> = dao.getFavoriteStations()
    fun isFavoriteStation(stationId: String): Flow<Boolean> {
        return dao.isFavoriteStation(stationId)
    }
}
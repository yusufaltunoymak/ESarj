package com.altunoymak.esarj.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteStationDao {
    @Query("SELECT * FROM favorite_stations")
    fun getFavoriteStations(): Flow<List<FavoriteStationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavoriteStation(station: FavoriteStationEntity)

    @Delete
    suspend fun removeFavoriteStation(station: FavoriteStationEntity)

    @Query("SELECT EXISTS(SELECT * FROM favorite_stations WHERE id = :stationId)")
    fun isFavoriteStation(stationId: String): Flow<Boolean>
}
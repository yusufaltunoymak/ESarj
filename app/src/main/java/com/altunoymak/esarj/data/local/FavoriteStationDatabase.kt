package com.altunoymak.esarj.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities = [FavoriteStationEntity::class], version = 1, exportSchema = false)
abstract class FavoriteStationDatabase : RoomDatabase() {
    abstract fun favoriteStationDao(): FavoriteStationDao
}
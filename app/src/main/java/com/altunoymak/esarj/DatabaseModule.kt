package com.altunoymak.esarj

import android.content.Context
import androidx.room.Room
import com.altunoymak.esarj.data.local.FavoriteStationDao
import com.altunoymak.esarj.data.local.FavoriteStationDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideFavoriteStationDatabase(@ApplicationContext context : Context) : FavoriteStationDatabase {
        return Room.databaseBuilder(context, FavoriteStationDatabase::class.java,"favorite_station_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideFavoriteStationDao(database: FavoriteStationDatabase): FavoriteStationDao {
        return database.favoriteStationDao()
    }
}
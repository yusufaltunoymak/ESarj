package com.altunoymak.esarj.data

import com.altunoymak.esarj.data.model.chargingstation.ChargingStationResponse
import com.altunoymak.esarj.data.model.chargingstationdetail.DetailStation
import com.altunoymak.esarj.data.model.nearestchargingstation.NearestStationResponse
import com.altunoymak.esarj.data.model.searchstation.SearchStationResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ChargingAPI {
    @GET("v1/search")
    suspend fun getChargingStations(): ChargingStationResponse

    @GET("v1/charging-stations/{chargingStationId}/detail")
    suspend fun getChargingStationDetail(@Path("chargingStationId") id: String): DetailStation

    @GET("v1/search/nearest")
    suspend fun getNearestChargingStations(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
    ): NearestStationResponse

    @GET("v1/search/suggest")
    suspend fun getSearchSuggest(
        @Query("q") query: String
    ): SearchStationResponse
}
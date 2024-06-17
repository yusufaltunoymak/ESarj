package com.altunoymak.esarj.data.repository

import com.altunoymak.esarj.data.ChargingAPI
import com.altunoymak.esarj.data.model.chargingstation.ChargingStationResponse
import com.altunoymak.esarj.data.model.chargingstationdetail.DetailStation
import com.altunoymak.esarj.data.model.nearestchargingstation.NearestStationResponse
import com.altunoymak.esarj.data.model.searchstation.SearchStationResponse
import com.altunoymak.esarj.util.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ChargingStationRepository @Inject constructor(private val api: ChargingAPI) {
    suspend fun getChargingStations() : Flow<Response<ChargingStationResponse>> {
        return flow {
            emit(Response.Loading)
            try {
                val response = api.getChargingStations()
                emit(Response.Success(response))
            }   catch (e: IOException) {
                e.printStackTrace()
                emit(Response.Error(_message ="Error loading charging station"))
                return@flow
            }
            catch (e : HttpException) {
                e.printStackTrace()
                emit(Response.Error(_message = "Error loading charging station"))
                return@flow
            }
            catch (e : Exception) {
                e.printStackTrace()
                emit(Response.Error(_message = "Error loading charging station"))
                return@flow
            }
        }
    }

    suspend fun getChargingStationDetail(id: String): Flow<Response<DetailStation>> {
        return flow {
            emit(Response.Loading)
            try {
                val response = api.getChargingStationDetail(id)
                emit(Response.Success(response))
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Response.Error(_message = "Error loading charging station detail"))
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Response.Error(_message = "Error loading charging station detail"))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Response.Error(_message = "Error loading charging station detail"))
            }
        }
    }
    suspend fun getNearestChargingStations(lat: Double, lon: Double): Flow<Response<NearestStationResponse>> {
        return flow {
            emit(Response.Loading)
            try {
                val response = api.getNearestChargingStations(lat, lon)
                emit(Response.Success(response))
            } catch (e: HttpException) {
                if (e.code() == 500) {
                    emit(Response.Error("Sunucuda bir hata oluştu!"))
                } else {
                    emit(Response.Error(e.localizedMessage ?: "An error occured"))
                }
            } catch (e: Exception) {
                emit(Response.Error(e.localizedMessage ?: "An error occured"))
            }
        }
    }
    suspend fun search(query: String): Flow<Response<SearchStationResponse>> {
        return flow {
            emit(Response.Loading)
            try {
                val response = api.getSearchSuggest(query)
                emit(Response.Success(response))
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Response.Error(_message = "Error loading search results"))
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Response.Error(_message = "Error loading search results"))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Response.Error(_message = "Error loading search results"))
            }
        }
    }
}
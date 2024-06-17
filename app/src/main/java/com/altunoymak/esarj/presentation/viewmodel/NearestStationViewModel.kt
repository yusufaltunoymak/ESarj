package com.altunoymak.esarj.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.altunoymak.esarj.presentation.ui.map.ChargingViewState
import com.altunoymak.esarj.data.repository.ChargingStationRepository
import com.altunoymak.esarj.util.ResponseStatus
import com.altunoymak.esarj.util.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NearestStationViewModel @Inject constructor(private val repository: ChargingStationRepository) : ViewModel() {

    private var _viewState = MutableStateFlow(ChargingViewState())
    val viewState = _viewState.asStateFlow()

    fun fetchNearestChargingStations(lat: Double, lon: Double) {
        viewModelScope.launch {
            repository.getNearestChargingStations(lat, lon).collect { response ->
                when (response.status) {
                    ResponseStatus.SUCCESS -> {
                        response.data?.let { data ->
                            val updatedList = data.nearestChargingStations?.mapNotNull { station ->
                                station?.location?.let { location ->
                                    val stationLat = location.lat
                                    val stationLon = location.lon
                                    if (stationLat != null && stationLon != null) {
                                        station.copy(distance = Utils.haversine(lat, lon, stationLat, stationLon))
                                    } else {
                                        null
                                    }
                                }
                            } ?: emptyList()

                            _viewState.update { viewState ->
                                viewState.copy(
                                    isLoading = false,
                                    nearestList = updatedList,
                                    errorMessage = null
                                )
                            }
                        }
                    }
                    ResponseStatus.ERROR -> {
                        _viewState.update { viewState ->
                            viewState.copy(
                                isLoading = false,
                                nearestList = emptyList(),
                                errorMessage = response.message
                            )
                        }
                    }
                    else -> {
                        _viewState.update { viewState ->
                            viewState.copy(
                                isLoading = true,
                                nearestList = emptyList(),
                                errorMessage = null
                            )
                        }
                    }
                }
            }
        }
    }
}
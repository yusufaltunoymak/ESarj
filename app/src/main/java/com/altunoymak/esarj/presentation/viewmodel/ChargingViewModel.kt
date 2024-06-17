package com.altunoymak.esarj.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.altunoymak.esarj.presentation.ui.map.ChargingViewState
import com.altunoymak.esarj.data.repository.ChargingStationRepository
import com.altunoymak.esarj.util.ResponseStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChargingViewModel @Inject constructor(private val repository: ChargingStationRepository) :
    ViewModel() {
    private var _viewState = MutableStateFlow(ChargingViewState())
    val viewState = _viewState.asStateFlow()

    fun getChargingStations() {
        viewModelScope.launch {
            repository.getChargingStations().collect { response ->
                when (response.status) {
                    ResponseStatus.SUCCESS -> {
                        response.data?.let {
                            _viewState.update { viewState ->
                                viewState.copy(
                                    isLoading = false,
                                    chargingList = it.chargingStations,
                                    errorMessage = null

                                )
                            }
                        }
                    }

                    ResponseStatus.ERROR -> {
                        _viewState.update { viewState ->
                            viewState.copy(
                                isLoading = false,
                                chargingList = emptyList(),
                                errorMessage = response.message
                            )
                        }
                    }

                    else -> {
                        _viewState.update { viewState ->
                            viewState.copy(
                                isLoading = true,
                                chargingList = emptyList(),
                                errorMessage = null
                            )
                        }
                    }

                }
            }
        }
    }

    fun fetchChargingStationDetail(id: String) {
        viewModelScope.launch {
            repository.getChargingStationDetail(id).collect { response ->
                when (response.status) {
                    ResponseStatus.SUCCESS -> {
                        response.data?.let { detailStation ->
                            _viewState.update { viewState ->
                                viewState.copy(
                                    isLoading = false,
                                    chargingDetail = detailStation,
                                )
                            }
                        }
                    }
                    ResponseStatus.ERROR -> {
                        _viewState.update { viewState ->
                            viewState.copy(
                                isLoading = false,
                                chargingDetail = null,
                                errorMessage = response.message
                            )
                        }
                    }
                    else -> {
                        _viewState.update { viewState ->
                            viewState.copy(
                                isLoading = true,
                                chargingDetail = null,
                                errorMessage = null
                            )
                        }
                    }
                }
            }
        }
    }

    fun clearChargingDetail() {
        _viewState.update { viewState ->
            viewState.copy(
                isLoading = false,
                chargingDetail = null,
                errorMessage = null
            )
        }
    }
}
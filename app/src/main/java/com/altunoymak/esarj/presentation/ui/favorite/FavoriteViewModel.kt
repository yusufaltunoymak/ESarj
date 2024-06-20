package com.altunoymak.esarj.presentation.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.altunoymak.esarj.data.local.FavoriteStationEntity
import com.altunoymak.esarj.data.repository.FavoriteStationRepository
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val repository: FavoriteStationRepository) : ViewModel() {

    private val _selectedStation = MutableLiveData<FavoriteStationEntity>()
    val selectedStation: LiveData<FavoriteStationEntity> get() = _selectedStation

    private val _selectedStationLocation = MutableLiveData<LatLng>()
    val selectedStationLocation: LiveData<LatLng> get() = _selectedStationLocation

    fun selectStation(station: FavoriteStationEntity) {
        _selectedStation.value = station
        _selectedStationLocation.value = LatLng(station.lat!!, station.lon!!)
    }

    val favoriteStations: Flow<List<FavoriteStationEntity>> = repository.getFavoriteStations()

    fun addFavoriteStation(station: FavoriteStationEntity) = viewModelScope.launch {
        repository.addFavoriteStation(station)
    }

    fun removeFavoriteStation(station: FavoriteStationEntity) = viewModelScope.launch {
        repository.removeFavoriteStation(station)
    }

    fun isFavoriteStation(stationId: String) = repository.isFavoriteStation(stationId).asLiveData()



}
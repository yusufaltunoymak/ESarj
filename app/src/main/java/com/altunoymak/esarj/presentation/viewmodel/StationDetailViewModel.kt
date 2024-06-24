package com.altunoymak.esarj.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.altunoymak.esarj.data.model.chargingstationdetail.DetailStation

class StationDetailViewModel : ViewModel() {
    private val _detailStation = MutableLiveData<DetailStation>()
    val detailStation: LiveData<DetailStation> = _detailStation

    fun setDetailStation(detailStation: DetailStation) {
        _detailStation.value = detailStation
    }
}
package com.altunoymak.esarj.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.altunoymak.esarj.data.model.searchstation.Suggestion
import com.altunoymak.esarj.data.repository.ChargingStationRepository
import com.altunoymak.esarj.presentation.ui.search.SearchViewState
import com.altunoymak.esarj.util.ResponseStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: ChargingStationRepository) :
    ViewModel() {

    private val _viewState = MutableStateFlow(SearchViewState())
    val viewState: StateFlow<SearchViewState> = _viewState

    private val _selectedSuggestion = MutableLiveData<Suggestion>()
    val selectedSuggestion: LiveData<Suggestion> get() = _selectedSuggestion
    fun selectSuggestion(suggestion: Suggestion) {
        _selectedSuggestion.value = suggestion
    }
    fun search(query: String) {
        viewModelScope.launch {
            repository.search(query).collect { response ->
                when (response.status) {
                    ResponseStatus.SUCCESS -> {
                        response.data?.let {
                            _viewState.update { viewState ->
                                viewState.copy(
                                    isLoading = false,
                                    suggestions = it.suggestions,
                                    errorMessage = null
                                )
                            }
                        }
                    }

                    ResponseStatus.LOADING -> {
                        _viewState.update { viewState ->
                            viewState.copy(
                                isLoading = true,
                                suggestions = null,
                                errorMessage = null
                            )
                        }
                    }

                    else -> {
                        _viewState.update { viewState ->
                            viewState.copy(
                                isLoading = false,
                                suggestions = null,
                                errorMessage = response.message
                            )
                        }
                    }
                }
            }
        }
    }
}
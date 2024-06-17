package com.altunoymak.esarj.presentation.ui.search

import com.altunoymak.esarj.data.model.searchstation.Suggestion

data class SearchViewState(
    val isLoading: Boolean? = null,
    val suggestions: List<Suggestion?>? = null,
    val errorMessage: String? = null
)
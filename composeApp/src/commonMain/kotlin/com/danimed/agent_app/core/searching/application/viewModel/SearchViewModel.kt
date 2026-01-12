package com.danimed.agent_app.core.searching.application.viewModel

import androidx.compose.runtime.*
import com.danimed.agent_app.core.searching.domain.model.SearchQuery
import com.danimed.agent_app.core.searching.domain.usecase.ClearRecentSearchesUseCase
import com.danimed.agent_app.core.searching.domain.usecase.GetRecentSearchesUseCase
import com.danimed.agent_app.core.searching.domain.usecase.SaveRecentSearchUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class SearchUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val recentSearches: List<SearchQuery> = emptyList(),
    val searchQuery: String = ""
)

class SearchViewModel(
    private val getRecentSearchesUseCase: GetRecentSearchesUseCase,
    private val saveRecentSearchUseCase: SaveRecentSearchUseCase,
    private val clearRecentSearchesUseCase: ClearRecentSearchesUseCase
) {
    private val _uiState = mutableStateOf(SearchUiState())
    val uiState: State<SearchUiState> = _uiState

    init {
        loadRecentSearches()
    }

    fun loadRecentSearches() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        
        CoroutineScope(Dispatchers.Default).launch {
            getRecentSearchesUseCase()
                .onSuccess { searches ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        recentSearches = searches,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Error al cargar búsquedas recientes"
                    )
                }
        }
    }

    fun saveSearch(query: String) {
        if (query.isBlank()) return
        
        CoroutineScope(Dispatchers.Default).launch {
            saveRecentSearchUseCase(query)
                .onSuccess {
                    loadRecentSearches()
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = exception.message ?: "Error al guardar búsqueda"
                    )
                }
        }
    }

    fun clearRecentSearches() {
        CoroutineScope(Dispatchers.Default).launch {
            clearRecentSearchesUseCase()
                .onSuccess {
                    _uiState.value = _uiState.value.copy(recentSearches = emptyList())
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = exception.message ?: "Error al limpiar búsquedas"
                    )
                }
        }
    }

    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}


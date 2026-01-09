package com.danimed.agent_app.shared.networks

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object NetworkErrorHandler {
    private val _hasNoInternet = MutableStateFlow(false)
    val hasNoInternet: StateFlow<Boolean> = _hasNoInternet.asStateFlow()
    
    private val _hasServerError = MutableStateFlow(false)
    val hasServerError: StateFlow<Boolean> = _hasServerError.asStateFlow()
    
    fun handleNetworkError(error: NetworkError) {
        when (error) {
            is NetworkError.NoConnection -> {
                _hasNoInternet.value = true
                _hasServerError.value = false
            }
            is NetworkError.ServerError -> {
                _hasNoInternet.value = false
                _hasServerError.value = true
            }
            else -> {
                // Para otros errores, no mostramos la pantalla de no internet
                _hasNoInternet.value = false
                _hasServerError.value = false
            }
        }
    }
    
    fun clearError() {
        _hasNoInternet.value = false
        _hasServerError.value = false
    }
    
    fun clearNoInternet() {
        _hasNoInternet.value = false
    }
    
    fun clearServerError() {
        _hasServerError.value = false
    }
}









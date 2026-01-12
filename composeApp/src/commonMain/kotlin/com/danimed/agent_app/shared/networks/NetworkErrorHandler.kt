package com.danimed.agent_app.shared.networks

import com.danimed.agent_app.shared.utils.NetworkConnectivityManagerProvider
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
                // Verificar si realmente hay conexión antes de marcar como NoConnection
                // Si hay conexión pero falló la petición, es un error de servidor
                val hasConnection = try {
                    // Asegurar que el provider esté inicializado
                    NetworkConnectivityManagerProvider.init()
                    NetworkConnectivityManagerProvider.getNetworkConnectivityManager().hasInternetConnection()
                } catch (e: Exception) {
                    // Si no se puede verificar, asumir que no hay conexión
                    false
                }
                
                if (hasConnection) {
                    // Hay conexión pero falló la petición → Error de servidor
                    _hasNoInternet.value = false
                    _hasServerError.value = true
                } else {
                    // No hay conexión → NoInternet
                    _hasNoInternet.value = true
                    _hasServerError.value = false
                }
            }
            is NetworkError.ServerError -> {
                _hasNoInternet.value = false
                _hasServerError.value = true
            }
            is NetworkError.Unknown -> {
                // Para errores desconocidos, verificar conexión para clasificar correctamente
                // Si el mensaje sugiere un error de conexión, verificar si realmente hay conexión
                val isConnectionError = error.message?.contains("Failed to connect", ignoreCase = true) == true ||
                                      error.message?.contains("Connection refused", ignoreCase = true) == true ||
                                      error.message?.contains("Connection timed out", ignoreCase = true) == true ||
                                      error.message?.contains("Network error", ignoreCase = true) == true ||
                                      error.message?.contains("Error de conexión", ignoreCase = true) == true
                
                if (isConnectionError) {
                    val hasConnection = try {
                        // Asegurar que el provider esté inicializado
                        NetworkConnectivityManagerProvider.init()
                        NetworkConnectivityManagerProvider.getNetworkConnectivityManager().hasInternetConnection()
                    } catch (e: Exception) {
                        false
                    }
                    
                    if (hasConnection) {
                        // Hay conexión pero falló la petición → Error de servidor
                        _hasNoInternet.value = false
                        _hasServerError.value = true
                    } else {
                        // No hay conexión
                        _hasNoInternet.value = true
                        _hasServerError.value = false
                    }
                } else {
                    // Otros errores desconocidos, asumir error de servidor si hay conexión
                    val hasConnection = try {
                        NetworkConnectivityManagerProvider.init()
                        NetworkConnectivityManagerProvider.getNetworkConnectivityManager().hasInternetConnection()
                    } catch (e: Exception) {
                        false
                    }
                    
                    if (hasConnection) {
                        _hasNoInternet.value = false
                        _hasServerError.value = true
                    } else {
                        _hasNoInternet.value = true
                        _hasServerError.value = false
                    }
                }
            }
            else -> {
                // Para otros errores (como Unauthorized), no mostrar pantallas de error
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









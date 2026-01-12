package com.danimed.agent_app.shared.utils

import kotlinx.coroutines.flow.StateFlow

/**
 * Interfaz para detectar el estado de conectividad a internet usando APIs nativas de la plataforma.
 * No hace fetch a ninguna página web, solo verifica el estado de la red del sistema.
 */
expect class NetworkConnectivityManager {
    /**
     * Flow que emite el estado actual de conectividad a internet.
     * true = hay conexión a internet disponible
     * false = no hay conexión a internet
     */
    val isConnected: StateFlow<Boolean>
    
    /**
     * Verifica si hay conexión a internet en este momento.
     * Esta es una verificación síncrona del estado actual.
     */
    fun hasInternetConnection(): Boolean
    
    /**
     * Inicia el monitoreo de conectividad.
     * Debe ser llamado cuando se necesita empezar a observar cambios en la conectividad.
     */
    fun startMonitoring()
    
    /**
     * Detiene el monitoreo de conectividad.
     * Debe ser llamado cuando ya no se necesita observar cambios en la conectividad.
     */
    fun stopMonitoring()
}

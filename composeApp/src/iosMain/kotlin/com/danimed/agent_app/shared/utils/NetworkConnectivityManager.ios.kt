package com.danimed.agent_app.shared.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import platform.Network.NWPathMonitor
import platform.Network.nw_path_monitor_cancel
import platform.Network.nw_path_monitor_create
import platform.Network.nw_path_monitor_set_queue
import platform.Network.nw_path_monitor_set_update_handler
import platform.Network.nw_path_get_status
import platform.Network.nw_path_status_satisfied
import platform.darwin.dispatch_queue_create
import platform.darwin.dispatch_queue_t
import platform.darwin.dispatch_release

actual class NetworkConnectivityManager {
    private var monitor: NWPathMonitor? = null
    private var queue: dispatch_queue_t? = null
    private val _isConnected = MutableStateFlow(false)
    actual val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()
    
    private var isMonitoring = false
    
    actual fun hasInternetConnection(): Boolean {
        return _isConnected.value
    }
    
    actual fun startMonitoring() {
        if (isMonitoring) return
        
        isMonitoring = true
        
        // Crear el monitor y la cola de despacho
        monitor = nw_path_monitor_create()
        queue = dispatch_queue_create("com.danimed.network.monitor", null)
        
        monitor?.let { mon ->
            queue?.let { q ->
                nw_path_monitor_set_queue(mon, q)
                nw_path_monitor_set_update_handler(mon) { path ->
                    val status = nw_path_get_status(path)
                    val isConnected = status == nw_path_status_satisfied
                    _isConnected.value = isConnected
                }
            }
        }
    }
    
    actual fun stopMonitoring() {
        if (!isMonitoring) return
        
        isMonitoring = false
        
        monitor?.let { mon ->
            nw_path_monitor_cancel(mon)
        }
        monitor = null
        
        queue?.let { q ->
            dispatch_release(q)
        }
        queue = null
    }
}

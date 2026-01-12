package com.danimed.agent_app.shared.utils

import android.content.Context

actual object NetworkConnectivityManagerProvider {
    private var context: Context? = null
    private var manager: NetworkConnectivityManager? = null

    fun init(context: Context) {
        NetworkConnectivityManagerProvider.context = context
        manager = NetworkConnectivityManager(context)
    }

    actual fun init() {
        // No-op, se inicializa con init(context)
    }

    actual fun getNetworkConnectivityManager(): NetworkConnectivityManager {
        return manager ?: throw IllegalStateException("NetworkConnectivityManagerProvider not initialized")
    }
}

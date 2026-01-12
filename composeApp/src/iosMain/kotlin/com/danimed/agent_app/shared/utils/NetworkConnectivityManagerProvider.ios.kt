package com.danimed.agent_app.shared.utils

actual object NetworkConnectivityManagerProvider {
    private var manager: NetworkConnectivityManager? = null

    actual fun init() {
        if (manager == null) {
            manager = NetworkConnectivityManager()
        }
    }

    actual fun getNetworkConnectivityManager(): NetworkConnectivityManager {
        if (manager == null) {
            manager = NetworkConnectivityManager()
        }
        return manager!!
    }
}

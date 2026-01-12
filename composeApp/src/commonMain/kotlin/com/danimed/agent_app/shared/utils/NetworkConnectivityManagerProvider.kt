package com.danimed.agent_app.shared.utils

expect object NetworkConnectivityManagerProvider {
    fun init()
    fun getNetworkConnectivityManager(): NetworkConnectivityManager
}

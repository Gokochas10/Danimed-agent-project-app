package com.danimed.agent_app.shared.utils

expect object TokenManagerProvider {
    fun getTokenManager(): TokenManager
}

interface TokenManager {
    fun saveToken(token: String)
    fun getToken(): String?
    fun clearToken()
    fun hasValidToken(): Boolean
}


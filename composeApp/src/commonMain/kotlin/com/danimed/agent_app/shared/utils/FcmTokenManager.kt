package com.danimed.agent_app.shared.utils

expect object FcmTokenManagerProvider {
    fun getFcmTokenManager(): FcmTokenManager
}

interface FcmTokenManager {
    fun saveToken(token: String)
    fun getToken(): String?
    fun clearToken()
    fun hasToken(): Boolean
}

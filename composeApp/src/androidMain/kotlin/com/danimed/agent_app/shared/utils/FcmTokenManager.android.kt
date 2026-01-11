package com.danimed.agent_app.shared.utils

import android.content.Context
import android.content.SharedPreferences

class FcmTokenManagerImpl(private val context: Context) : FcmTokenManager {
    private val prefs: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val FCM_TOKEN_KEY = "fcm_token"

    override fun saveToken(token: String) {
        prefs.edit().putString(FCM_TOKEN_KEY, token).apply()
    }

    override fun getToken(): String? {
        return prefs.getString(FCM_TOKEN_KEY, null)
    }

    override fun clearToken() {
        prefs.edit().remove(FCM_TOKEN_KEY).apply()
    }

    override fun hasToken(): Boolean {
        val token = getToken()
        return !token.isNullOrBlank()
    }
}

actual object FcmTokenManagerProvider {
    private var context: Context? = null

    fun init(context: Context) {
        FcmTokenManagerProvider.context = context
    }

    actual fun getFcmTokenManager(): FcmTokenManager {
        return FcmTokenManagerImpl(context ?: throw IllegalStateException("FcmTokenManagerProvider not initialized"))
    }
}

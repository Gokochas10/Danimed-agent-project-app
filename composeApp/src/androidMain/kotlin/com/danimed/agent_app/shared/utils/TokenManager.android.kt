package com.danimed.agent_app.shared.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.platform.LocalContext
import com.danimed.agent_app.shared.conf.AppConfig

class TokenManagerImpl(private val context: Context) : TokenManager {
    private val prefs: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    override fun saveToken(token: String) {
        prefs.edit().putString(AppConfig.TOKEN_KEY, token).apply()
    }

    override fun getToken(): String? {
        return prefs.getString(AppConfig.TOKEN_KEY, null)
    }

    override fun clearToken() {
        prefs.edit().remove(AppConfig.TOKEN_KEY).apply()
    }

    override fun hasValidToken(): Boolean {
        val token = getToken()
        return !token.isNullOrBlank()
    }
}

actual object TokenManagerProvider {
    private var context: Context? = null

    fun init(context: Context) {
        TokenManagerProvider.context = context
    }

    actual fun getTokenManager(): TokenManager {
        return TokenManagerImpl(context ?: throw IllegalStateException("TokenManagerProvider not initialized"))
    }
}


package com.danimed.agent_app.shared.utils

import platform.Foundation.NSUserDefaults
import com.danimed.agent_app.shared.conf.AppConfig

class TokenManagerImpl : TokenManager {
    private val userDefaults = NSUserDefaults.standardUserDefaults

    override fun saveToken(token: String) {
        userDefaults.setObject(token, AppConfig.TOKEN_KEY)
    }

    override fun getToken(): String? {
        return userDefaults.objectForKey(AppConfig.TOKEN_KEY) as? String
    }

    override fun clearToken() {
        userDefaults.removeObjectForKey(AppConfig.TOKEN_KEY)
    }

    override fun hasValidToken(): Boolean {
        val token = getToken()
        return !token.isNullOrBlank()
    }
}

actual object TokenManagerProvider {
    actual fun getTokenManager(): TokenManager {
        return TokenManagerImpl()
    }
}


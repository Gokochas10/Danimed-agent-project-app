package com.danimed.agent_app.shared.utils

import platform.Foundation.NSUserDefaults

class FcmTokenManagerImpl : FcmTokenManager {
    private val userDefaults = NSUserDefaults.standardUserDefaults
    private val FCM_TOKEN_KEY = "fcm_token"

    override fun saveToken(token: String) {
        userDefaults.setObject(token, FCM_TOKEN_KEY)
    }

    override fun getToken(): String? {
        return userDefaults.objectForKey(FCM_TOKEN_KEY) as? String
    }

    override fun clearToken() {
        userDefaults.removeObjectForKey(FCM_TOKEN_KEY)
    }

    override fun hasToken(): Boolean {
        val token = getToken()
        return !token.isNullOrBlank()
    }
}

actual object FcmTokenManagerProvider {
    actual fun getFcmTokenManager(): FcmTokenManager {
        return FcmTokenManagerImpl()
    }
}

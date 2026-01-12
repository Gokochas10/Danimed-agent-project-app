package com.danimed.agent_app.shared.utils

import platform.Foundation.NSUserDefaults

class CredentialsManagerImpl : CredentialsManager {
    private val userDefaults = NSUserDefaults.standardUserDefaults
    private val usernameKey = "saved_username"
    private val passwordKey = "saved_password"
    private val json = Json { ignoreUnknownKeys = true }
    
    override fun saveCredentials(username: String, password: String) {
        // En iOS, guardamos de forma simple (en producción usar Keychain)
        userDefaults.setObject(username, usernameKey)
        userDefaults.setObject(password, passwordKey)
    }
    
    override fun getCredentials(): Pair<String, String>? {
        val username = userDefaults.objectForKey(usernameKey) as? String
        val password = userDefaults.objectForKey(passwordKey) as? String
        
        if (username == null || password == null) {
            return null
        }
        
        return Pair(username, password)
    }
    
    override fun clearCredentials() {
        userDefaults.removeObjectForKey(usernameKey)
        userDefaults.removeObjectForKey(passwordKey)
    }
    
    override fun hasCredentials(): Boolean {
        return userDefaults.objectForKey(usernameKey) != null && 
               userDefaults.objectForKey(passwordKey) != null
    }
}

actual object CredentialsManagerProvider {
    actual fun getCredentialsManager(): CredentialsManager {
        return CredentialsManagerImpl()
    }
}


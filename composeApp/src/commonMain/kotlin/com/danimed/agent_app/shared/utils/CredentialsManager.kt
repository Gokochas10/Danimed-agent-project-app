package com.danimed.agent_app.shared.utils

expect object CredentialsManagerProvider {
    fun getCredentialsManager(): CredentialsManager
}

interface CredentialsManager {
    fun saveCredentials(username: String, password: String)
    fun getCredentials(): Pair<String, String>?
    fun clearCredentials()
    fun hasCredentials(): Boolean
}



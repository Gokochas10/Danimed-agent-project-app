package com.danimed.agent_app.shared.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.platform.LocalContext
import com.danimed.agent_app.shared.conf.AppConfig
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import android.util.Base64

class CredentialsManagerImpl(private val context: Context) : CredentialsManager {
    private val prefs: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val usernameKey = "saved_username"
    private val passwordKey = "saved_password"
    
    // Clave simple para encriptación (en producción usar una clave más segura)
    private val secretKey = SecretKeySpec("DanimedAgent2024!".toByteArray(), "AES")
    
    override fun saveCredentials(username: String, password: String) {
        val encryptedUsername = encrypt(username)
        val encryptedPassword = encrypt(password)
        prefs.edit()
            .putString(usernameKey, encryptedUsername)
            .putString(passwordKey, encryptedPassword)
            .apply()
    }
    
    override fun getCredentials(): Pair<String, String>? {
        val encryptedUsername = prefs.getString(usernameKey, null)
        val encryptedPassword = prefs.getString(passwordKey, null)
        
        if (encryptedUsername == null || encryptedPassword == null) {
            return null
        }
        
        return try {
            val username = decrypt(encryptedUsername)
            val password = decrypt(encryptedPassword)
            Pair(username, password)
        } catch (e: Exception) {
            null
        }
    }
    
    override fun clearCredentials() {
        prefs.edit()
            .remove(usernameKey)
            .remove(passwordKey)
            .apply()
    }
    
    override fun hasCredentials(): Boolean {
        return prefs.contains(usernameKey) && prefs.contains(passwordKey)
    }
    
    private fun encrypt(value: String): String {
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val encrypted = cipher.doFinal(value.toByteArray())
        return Base64.encodeToString(encrypted, Base64.DEFAULT)
    }
    
    private fun decrypt(encryptedValue: String): String {
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        val decrypted = cipher.doFinal(Base64.decode(encryptedValue, Base64.DEFAULT))
        return String(decrypted)
    }
}

actual object CredentialsManagerProvider {
    private var context: Context? = null
    
    fun init(context: Context) {
        CredentialsManagerProvider.context = context
    }
    
    actual fun getCredentialsManager(): CredentialsManager {
        return CredentialsManagerImpl(context ?: throw IllegalStateException("CredentialsManagerProvider not initialized"))
    }
}



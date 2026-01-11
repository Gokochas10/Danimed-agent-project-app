package com.danimed.agent_app.shared.utils

import android.content.Context
import android.content.SharedPreferences
import com.danimed.agent_app.core.notifications.domain.model.Notification
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString

class NotificationsLocalManagerImpl(private val context: Context) : NotificationsLocalManager {
    private val prefs: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val json = Json { ignoreUnknownKeys = true }
    private val key = "notifications"
    private val maxNotifications = 100

    override fun saveNotification(notification: Notification) {
        val notifications = getNotifications().toMutableList()
        // Add to the beginning
        notifications.add(0, notification)
        // Keep only the last maxNotifications
        val trimmed = notifications.take(maxNotifications)
        val jsonString = json.encodeToString(trimmed)
        prefs.edit().putString(key, jsonString).apply()
    }

    override fun getNotifications(): List<Notification> {
        val jsonString = prefs.getString(key, null) ?: return emptyList()
        return try {
            json.decodeFromString<List<Notification>>(jsonString)
        } catch (e: Exception) {
            emptyList()
        }
    }

    override fun getUnreadCount(): Int {
        return getNotifications().count { !it.isRead }
    }

    override fun markAllAsRead() {
        // Eliminar todas las notificaciones al marcarlas como leídas
        clearNotifications()
    }

    override fun markAsRead(id: String) {
        val notifications = getNotifications().map { 
            if (it.id == id) it.copy(isRead = true) else it
        }
        val jsonString = json.encodeToString(notifications)
        prefs.edit().putString(key, jsonString).apply()
    }

    override fun clearNotifications() {
        prefs.edit().remove(key).apply()
    }
}

actual object NotificationsLocalManagerProvider {
    private var context: Context? = null

    fun init(context: Context) {
        NotificationsLocalManagerProvider.context = context
    }

    actual fun getNotificationsLocalManager(): NotificationsLocalManager {
        return NotificationsLocalManagerImpl(context ?: throw IllegalStateException("NotificationsLocalManagerProvider not initialized"))
    }
}

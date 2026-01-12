package com.danimed.agent_app.shared.utils

import com.danimed.agent_app.core.notifications.domain.model.Notification
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import platform.Foundation.NSUserDefaults

class NotificationsLocalManagerImpl : NotificationsLocalManager {
    private val userDefaults = NSUserDefaults.standardUserDefaults
    private val json = Json { ignoreUnknownKeys = true }
    private val key = "notifications"
    private val maxNotifications = 100

    override fun saveNotification(notification: Notification) {
        val notifications = getNotifications().toMutableList()
        notifications.add(0, notification)
        val trimmed = notifications.take(maxNotifications)
        val jsonString = json.encodeToString(trimmed)
        userDefaults.setObject(jsonString, key)
    }

    override fun getNotifications(): List<Notification> {
        val jsonString = userDefaults.objectForKey(key) as? String ?: return emptyList()
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
        userDefaults.setObject(jsonString, key)
    }

    override fun clearNotifications() {
        userDefaults.removeObjectForKey(key)
    }
}

actual object NotificationsLocalManagerProvider {
    actual fun getNotificationsLocalManager(): NotificationsLocalManager {
        return NotificationsLocalManagerImpl()
    }
}

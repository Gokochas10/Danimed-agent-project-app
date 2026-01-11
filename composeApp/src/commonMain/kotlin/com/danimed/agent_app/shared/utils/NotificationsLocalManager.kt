package com.danimed.agent_app.shared.utils

import com.danimed.agent_app.core.notifications.domain.model.Notification

expect object NotificationsLocalManagerProvider {
    fun getNotificationsLocalManager(): NotificationsLocalManager
}

interface NotificationsLocalManager {
    fun saveNotification(notification: Notification)
    fun getNotifications(): List<Notification>
    fun getUnreadCount(): Int
    fun markAllAsRead()
    fun markAsRead(id: String)
    fun clearNotifications()
}

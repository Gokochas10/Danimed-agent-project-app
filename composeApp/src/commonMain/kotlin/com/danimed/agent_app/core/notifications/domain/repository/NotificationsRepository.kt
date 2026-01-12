package com.danimed.agent_app.core.notifications.domain.repository

import com.danimed.agent_app.core.notifications.domain.model.Notification

interface NotificationsRepository {
    suspend fun getNotifications(): Result<List<Notification>>
    suspend fun getUnreadCount(): Result<Int>
    suspend fun markAllAsRead(): Result<Unit>
    suspend fun markAsRead(id: String): Result<Unit>
    suspend fun saveNotification(notification: Notification): Result<Unit>
    suspend fun clearNotifications(): Result<Unit>
}

package com.danimed.agent_app.core.notifications.infrastructure.datasource.local

import com.danimed.agent_app.core.notifications.domain.model.Notification
import com.danimed.agent_app.shared.utils.NotificationsLocalManagerProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotificationsLocalDataSource {
    private val manager = NotificationsLocalManagerProvider.getNotificationsLocalManager()
    
    suspend fun saveNotification(notification: Notification): Result<Unit> {
        return try {
            withContext(Dispatchers.Default) {
                manager.saveNotification(notification)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getNotifications(): Result<List<Notification>> {
        return try {
            val notifications = withContext(Dispatchers.Default) {
                manager.getNotifications()
            }
            Result.success(notifications)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getUnreadCount(): Result<Int> {
        return try {
            val count = withContext(Dispatchers.Default) {
                manager.getUnreadCount()
            }
            Result.success(count)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun markAllAsRead(): Result<Unit> {
        return try {
            withContext(Dispatchers.Default) {
                manager.markAllAsRead()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun markAsRead(id: String): Result<Unit> {
        return try {
            withContext(Dispatchers.Default) {
                manager.markAsRead(id)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun clearNotifications(): Result<Unit> {
        return try {
            withContext(Dispatchers.Default) {
                manager.clearNotifications()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

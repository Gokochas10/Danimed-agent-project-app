package com.danimed.agent_app.core.notifications.infrastructure.repository

import com.danimed.agent_app.core.notifications.domain.model.Notification
import com.danimed.agent_app.core.notifications.domain.repository.NotificationsRepository
import com.danimed.agent_app.core.notifications.infrastructure.datasource.local.NotificationsLocalDataSource

class NotificationsRepositoryImpl(
    private val localDataSource: NotificationsLocalDataSource
) : NotificationsRepository {
    
    override suspend fun getNotifications(): Result<List<Notification>> {
        return localDataSource.getNotifications()
    }
    
    override suspend fun getUnreadCount(): Result<Int> {
        return localDataSource.getUnreadCount()
    }
    
    override suspend fun markAllAsRead(): Result<Unit> {
        return localDataSource.markAllAsRead()
    }
    
    override suspend fun markAsRead(id: String): Result<Unit> {
        return localDataSource.markAsRead(id)
    }
    
    override suspend fun saveNotification(notification: Notification): Result<Unit> {
        return localDataSource.saveNotification(notification)
    }
    
    override suspend fun clearNotifications(): Result<Unit> {
        return localDataSource.clearNotifications()
    }
}

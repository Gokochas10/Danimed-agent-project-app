package com.danimed.agent_app.shared.di

import com.danimed.agent_app.core.notifications.domain.repository.NotificationsRepository
import com.danimed.agent_app.core.notifications.domain.usecase.GetNotificationsUseCase
import com.danimed.agent_app.core.notifications.domain.usecase.GetUnreadCountUseCase
import com.danimed.agent_app.core.notifications.domain.usecase.MarkAllAsReadUseCase
import com.danimed.agent_app.core.notifications.domain.usecase.SaveNotificationUseCase
import com.danimed.agent_app.core.notifications.infrastructure.datasource.local.NotificationsLocalDataSource
import com.danimed.agent_app.core.notifications.infrastructure.repository.NotificationsRepositoryImpl

object NotificationsModule {
    private val localDataSource = NotificationsLocalDataSource()
    private val repository: NotificationsRepository = NotificationsRepositoryImpl(localDataSource)
    
    val getNotificationsUseCase = GetNotificationsUseCase(repository)
    val getUnreadCountUseCase = GetUnreadCountUseCase(repository)
    val markAllAsReadUseCase = MarkAllAsReadUseCase(repository)
    val saveNotificationUseCase = SaveNotificationUseCase(repository)
}

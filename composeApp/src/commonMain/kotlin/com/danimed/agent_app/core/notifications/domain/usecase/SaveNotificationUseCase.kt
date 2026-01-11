package com.danimed.agent_app.core.notifications.domain.usecase

import com.danimed.agent_app.core.notifications.domain.model.Notification
import com.danimed.agent_app.core.notifications.domain.repository.NotificationsRepository

class SaveNotificationUseCase(
    private val repository: NotificationsRepository
) {
    suspend operator fun invoke(notification: Notification): Result<Unit> {
        return repository.saveNotification(notification)
    }
}

package com.danimed.agent_app.core.notifications.domain.usecase

import com.danimed.agent_app.core.notifications.domain.model.Notification
import com.danimed.agent_app.core.notifications.domain.repository.NotificationsRepository

class GetNotificationsUseCase(
    private val repository: NotificationsRepository
) {
    suspend operator fun invoke(): Result<List<Notification>> {
        return repository.getNotifications()
    }
}

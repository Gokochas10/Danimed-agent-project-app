package com.danimed.agent_app.core.notifications.domain.usecase

import com.danimed.agent_app.core.notifications.domain.repository.NotificationsRepository

class GetUnreadCountUseCase(
    private val repository: NotificationsRepository
) {
    suspend operator fun invoke(): Result<Int> {
        return repository.getUnreadCount()
    }
}

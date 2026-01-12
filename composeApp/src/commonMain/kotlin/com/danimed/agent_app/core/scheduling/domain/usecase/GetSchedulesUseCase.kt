package com.danimed.agent_app.core.scheduling.presentation.domain.usecase

import com.danimed.agent_app.core.scheduling.presentation.domain.model.Schedule
import com.danimed.agent_app.core.scheduling.presentation.domain.repository.ScheduleRepository

class GetSchedulesUseCase(
    private val scheduleRepository: com.danimed.agent_app.core.scheduling.presentation.domain.repository.ScheduleRepository
) {
    suspend operator fun invoke(doctorId: Int): Result<List<com.danimed.agent_app.core.scheduling.presentation.domain.model.Schedule>> {
        return scheduleRepository.getSchedules(doctorId)
    }
}





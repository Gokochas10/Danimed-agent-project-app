package com.danimed.agent_app.core.schedule.domain.usecase

import com.danimed.agent_app.core.schedule.domain.model.Schedule
import com.danimed.agent_app.core.schedule.domain.repository.ScheduleRepository

class GetSchedulesUseCase(
    private val scheduleRepository: ScheduleRepository
) {
    suspend operator fun invoke(doctorId: Int): Result<List<Schedule>> {
        return scheduleRepository.getSchedules(doctorId)
    }
}




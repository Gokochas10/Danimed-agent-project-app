package com.danimed.agent_app.core.schedule.domain.repository

import com.danimed.agent_app.core.schedule.domain.model.Schedule

interface ScheduleRepository {
    suspend fun getSchedules(doctorId: Int): Result<List<Schedule>>
}





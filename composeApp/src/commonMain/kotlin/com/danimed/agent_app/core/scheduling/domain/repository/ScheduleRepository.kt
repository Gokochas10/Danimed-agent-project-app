package com.danimed.agent_app.core.scheduling.presentation.domain.repository

import com.danimed.agent_app.core.scheduling.presentation.domain.model.Schedule

interface ScheduleRepository {
    suspend fun getSchedules(doctorId: Int): Result<List<com.danimed.agent_app.core.scheduling.presentation.domain.model.Schedule>>
}





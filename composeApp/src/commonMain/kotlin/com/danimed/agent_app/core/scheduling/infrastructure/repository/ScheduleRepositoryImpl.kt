package com.danimed.agent_app.core.scheduling.presentation.infrastructure.repository

import com.danimed.agent_app.core.scheduling.presentation.domain.model.Schedule
import com.danimed.agent_app.core.scheduling.presentation.domain.repository.ScheduleRepository
import com.danimed.agent_app.core.scheduling.presentation.infrastructure.datasource.remote.ScheduleRemoteDataSource

class ScheduleRepositoryImpl(
    private val remoteDataSource: com.danimed.agent_app.core.scheduling.presentation.infrastructure.datasource.remote.ScheduleRemoteDataSource
) : com.danimed.agent_app.core.scheduling.presentation.domain.repository.ScheduleRepository {
    override suspend fun getSchedules(doctorId: Int): Result<List<com.danimed.agent_app.core.scheduling.presentation.domain.model.Schedule>> {
        return remoteDataSource.getSchedules(doctorId)
    }
}





package com.danimed.agent_app.core.schedule.infrastructure.repository

import com.danimed.agent_app.core.schedule.domain.model.Schedule
import com.danimed.agent_app.core.schedule.domain.repository.ScheduleRepository
import com.danimed.agent_app.core.schedule.infrastructure.datasource.remote.ScheduleRemoteDataSource

class ScheduleRepositoryImpl(
    private val remoteDataSource: ScheduleRemoteDataSource
) : ScheduleRepository {
    override suspend fun getSchedules(doctorId: Int): Result<List<Schedule>> {
        return remoteDataSource.getSchedules(doctorId)
    }
}



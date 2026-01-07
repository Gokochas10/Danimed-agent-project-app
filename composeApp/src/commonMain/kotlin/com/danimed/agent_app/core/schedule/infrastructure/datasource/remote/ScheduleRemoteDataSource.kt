package com.danimed.agent_app.core.schedule.infrastructure.datasource.remote

import com.danimed.agent_app.core.schedule.application.dto.res.ScheduleResponse
import com.danimed.agent_app.core.schedule.domain.model.Schedule
import com.danimed.agent_app.core.schedule.infrastructure.api.ScheduleApi
import com.danimed.agent_app.shared.networks.dto.ApiRes

class ScheduleRemoteDataSource(private val scheduleApi: ScheduleApi) {
    suspend fun getSchedules(doctorId: Int): Result<List<Schedule>> {
        return scheduleApi.getSchedules(doctorId).fold(
            onSuccess = { apiRes ->
                if (apiRes.success && apiRes.data != null) {
                    Result.success(apiRes.data.map { it.toDomain() })
                } else {
                    Result.failure(Exception(apiRes.message.content.joinToString(", ")))
                }
            },
            onFailure = { Result.failure(it) }
        )
    }
}




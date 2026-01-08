package com.danimed.agent_app.core.scheduling.presentation.application.dto.res

import com.danimed.agent_app.core.scheduling.presentation.domain.model.Schedule
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleResponse(
    val id: Int,
    val date: String,
    val start_time: String,
    val end_time: String,
    val is_active: Boolean
) {
    fun toDomain(): com.danimed.agent_app.core.scheduling.presentation.domain.model.Schedule {
        return _root_ide_package_.com.danimed.agent_app.core.scheduling.presentation.domain.model.Schedule(
            id = id,
            date = date,
            start_time = start_time,
            end_time = end_time,
            is_active = is_active
        )
    }
}





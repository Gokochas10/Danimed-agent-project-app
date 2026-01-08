package com.danimed.agent_app.core.scheduling.presentation.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Schedule(
    val id: Int,
    val date: String, // Format: "2026-01-05"
    val start_time: String, // Format: "08:00:00"
    val end_time: String, // Format: "12:00:00"
    val is_active: Boolean
)





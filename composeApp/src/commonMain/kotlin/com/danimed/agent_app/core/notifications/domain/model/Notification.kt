package com.danimed.agent_app.core.notifications.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Notification(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: Long,
    val isRead: Boolean = false,
    val data: Map<String, String>? = null
)

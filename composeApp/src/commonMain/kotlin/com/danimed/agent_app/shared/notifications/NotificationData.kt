package com.danimed.agent_app.shared.notifications

import kotlinx.serialization.Serializable

@Serializable
data class PushNotificationPayload(
    val title: String,
    val body: String,
    val data: Map<String, String>? = null
)
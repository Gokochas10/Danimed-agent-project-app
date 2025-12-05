package com.danimed.agent_app.shared.networks.dto

import kotlinx.serialization.Serializable

@Serializable
data class ApiRes<T>(
    val success: Boolean,
    val message: ApiMessage,
    val data: T? = null
)


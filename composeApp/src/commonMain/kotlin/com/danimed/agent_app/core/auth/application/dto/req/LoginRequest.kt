package com.danimed.agent_app.core.auth.application.dto.req

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)













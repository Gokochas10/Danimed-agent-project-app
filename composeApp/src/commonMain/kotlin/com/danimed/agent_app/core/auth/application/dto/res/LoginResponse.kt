package com.danimed.agent_app.core.auth.application.dto.res

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("access_token")
    val token: String,
    @SerialName("token_type")
    val tokenType: String? = null,
    @SerialName("user_id")
    val userId: Int? = null,
    val username: String? = null,
    val role: String? = null
)


package com.danimed.agent_app.core.auth.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val user_id: String,
    val username: String,
    val role: String
)



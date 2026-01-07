package com.danimed.agent_app.core.profile.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val full_name: String,
    val phone: String,
    val email: String,
    val username: String,
    val role: String,
    val is_active: Boolean
)



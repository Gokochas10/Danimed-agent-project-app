package com.danimed.agent_app.core.profile.application.dto.res

import com.danimed.agent_app.core.profile.domain.model.Profile
import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    val full_name: String,
    val phone: String,
    val email: String,
    val username: String,
    val role: String,
    val is_active: Boolean
) {
    fun toDomain(): Profile {
        return Profile(
            full_name = full_name,
            phone = phone,
            email = email,
            username = username,
            role = role,
            is_active = is_active
        )
    }
}



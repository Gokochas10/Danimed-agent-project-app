package com.danimed.agent_app.core.profile.domain.repository

import com.danimed.agent_app.core.profile.domain.model.Profile

interface ProfileRepository {
    suspend fun getProfile(): Result<Profile>
}




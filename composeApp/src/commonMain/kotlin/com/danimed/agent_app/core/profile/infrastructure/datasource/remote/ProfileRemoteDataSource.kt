package com.danimed.agent_app.core.profile.infrastructure.datasource.remote

import com.danimed.agent_app.core.profile.application.dto.res.ProfileResponse
import com.danimed.agent_app.core.profile.domain.model.Profile
import com.danimed.agent_app.core.profile.infrastructure.api.ProfileApi
import com.danimed.agent_app.shared.networks.dto.ApiRes

class ProfileRemoteDataSource(private val profileApi: ProfileApi) {
    suspend fun getProfile(): Result<Profile> {
        return profileApi.getProfile().fold(
            onSuccess = { apiRes ->
                if (apiRes.success && apiRes.data != null) {
                    Result.success(apiRes.data.toDomain())
                } else {
                    Result.failure(Exception(apiRes.message.content.joinToString(", ")))
                }
            },
            onFailure = { Result.failure(it) }
        )
    }
}



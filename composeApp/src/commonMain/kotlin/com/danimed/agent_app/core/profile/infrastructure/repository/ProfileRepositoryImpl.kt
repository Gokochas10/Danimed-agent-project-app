package com.danimed.agent_app.core.profile.infrastructure.repository

import com.danimed.agent_app.core.profile.domain.model.Profile
import com.danimed.agent_app.core.profile.domain.repository.ProfileRepository
import com.danimed.agent_app.core.profile.infrastructure.datasource.remote.ProfileRemoteDataSource

class ProfileRepositoryImpl(
    private val remoteDataSource: ProfileRemoteDataSource
) : ProfileRepository {
    override suspend fun getProfile(): Result<Profile> {
        return remoteDataSource.getProfile()
    }
}




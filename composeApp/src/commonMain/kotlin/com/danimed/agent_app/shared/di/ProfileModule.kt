package com.danimed.agent_app.shared.di

import com.danimed.agent_app.core.profile.domain.repository.ProfileRepository
import com.danimed.agent_app.core.profile.domain.usecase.GetProfileUseCase
import com.danimed.agent_app.core.profile.infrastructure.api.ProfileApi
import com.danimed.agent_app.core.profile.infrastructure.datasource.remote.ProfileRemoteDataSource
import com.danimed.agent_app.core.profile.infrastructure.repository.ProfileRepositoryImpl
import com.danimed.agent_app.shared.networks.createHttpClient

object ProfileModule {
    private val httpClient = createHttpClient()
    private val profileApi = ProfileApi(httpClient)
    private val profileRemoteDataSource = ProfileRemoteDataSource(profileApi)
    val profileRepository: ProfileRepository = ProfileRepositoryImpl(profileRemoteDataSource)
    val getProfileUseCase = GetProfileUseCase(profileRepository)
}





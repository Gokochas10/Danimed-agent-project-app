package com.danimed.agent_app.shared.di

import com.danimed.agent_app.core.auth.domain.repository.AuthRepository
import com.danimed.agent_app.core.auth.domain.usecase.GetCurrentUserUseCase
import com.danimed.agent_app.core.auth.domain.usecase.LoginUseCase
import com.danimed.agent_app.core.auth.infrastructure.api.AuthApi
import com.danimed.agent_app.core.auth.infrastructure.datasource.remote.AuthRemoteDataSource
import com.danimed.agent_app.core.auth.infrastructure.repository.AuthRepositoryImpl
import com.danimed.agent_app.shared.networks.createHttpClient

object AuthModule {
    private val httpClient = createHttpClient()
    private val authApi = AuthApi(httpClient)
    private val authRemoteDataSource = AuthRemoteDataSource(authApi)
    val authRepository: AuthRepository = AuthRepositoryImpl(authRemoteDataSource)
    val loginUseCase = LoginUseCase(authRepository)
    val getCurrentUserUseCase = GetCurrentUserUseCase(authRepository)
}














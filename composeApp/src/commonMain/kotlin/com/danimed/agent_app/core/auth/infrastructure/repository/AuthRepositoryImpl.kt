package com.danimed.agent_app.core.auth.infrastructure.repository

import com.danimed.agent_app.core.auth.domain.model.User
import com.danimed.agent_app.core.auth.domain.repository.AuthRepository
import com.danimed.agent_app.core.auth.infrastructure.datasource.remote.AuthRemoteDataSource

class AuthRepositoryImpl(
    private val remoteDataSource: AuthRemoteDataSource
) : AuthRepository {
    override suspend fun login(username: String, password: String): Result<String> {
        return remoteDataSource.login(username, password)
    }

    override suspend fun getCurrentUser(token: String): Result<User> {
        return remoteDataSource.getCurrentUser(token)
    }
}


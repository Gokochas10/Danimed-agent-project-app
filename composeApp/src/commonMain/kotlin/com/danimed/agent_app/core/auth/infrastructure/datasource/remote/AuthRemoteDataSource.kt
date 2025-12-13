package com.danimed.agent_app.core.auth.infrastructure.datasource.remote

import com.danimed.agent_app.core.auth.application.dto.req.LoginRequest
import com.danimed.agent_app.core.auth.application.dto.res.LoginResponse
import com.danimed.agent_app.core.auth.domain.model.User
import com.danimed.agent_app.core.auth.infrastructure.api.AuthApi
import com.danimed.agent_app.shared.networks.dto.ApiRes

class AuthRemoteDataSource(private val authApi: AuthApi) {
    suspend fun login(username: String, password: String): Result<String> {
        return authApi.login(LoginRequest(username, password)).fold(
            onSuccess = { apiRes ->
                if (apiRes.success && apiRes.data != null) {
                    Result.success(apiRes.data.token)
                } else {
                    Result.failure(Exception(apiRes.message.content.joinToString(", ")))
                }
            },
            onFailure = { Result.failure(it) }
        )
    }

    suspend fun getCurrentUser(token: String): Result<User> {
        return authApi.getCurrentUser(token).fold(
            onSuccess = { apiRes ->
                if (apiRes.success && apiRes.data != null) {
                    Result.success(apiRes.data)
                } else {
                    Result.failure(Exception(apiRes.message.content.joinToString(", ")))
                }
            },
            onFailure = { Result.failure(it) }
        )
    }
}









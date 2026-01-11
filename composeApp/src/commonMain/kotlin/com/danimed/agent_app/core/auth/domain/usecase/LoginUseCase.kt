package com.danimed.agent_app.core.auth.domain.usecase

import com.danimed.agent_app.core.auth.domain.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        username: String,
        password: String,
        fcmToken: String? = null,
        platform: String? = null
    ): Result<String> {
        return authRepository.login(username, password, fcmToken, platform)
    }
}















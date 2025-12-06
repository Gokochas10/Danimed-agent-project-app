package com.danimed.agent_app.core.auth.domain.usecase

import com.danimed.agent_app.core.auth.domain.model.User
import com.danimed.agent_app.core.auth.domain.repository.AuthRepository

class GetCurrentUserUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(token: String): Result<User> {
        return authRepository.getCurrentUser(token)
    }
}



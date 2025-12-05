package com.danimed.agent_app.core.auth.domain.repository

import com.danimed.agent_app.core.auth.domain.model.User

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<String>
    suspend fun getCurrentUser(token: String): Result<User>
}


package com.danimed.agent_app.core.auth.infrastructure.api

import com.danimed.agent_app.core.auth.application.dto.req.LoginRequest
import com.danimed.agent_app.core.auth.application.dto.res.LoginResponse
import com.danimed.agent_app.core.auth.domain.model.User
import com.danimed.agent_app.shared.conf.AppConfig
import com.danimed.agent_app.shared.networks.dto.ApiRes
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class AuthApi(private val httpClient: HttpClient) {
    suspend fun login(request: LoginRequest): Result<ApiRes<LoginResponse>> {
        return try {
            val response = httpClient.post("${AppConfig.API_BASE_URL}/api/v1/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body<ApiRes<LoginResponse>>()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCurrentUser(token: String): Result<ApiRes<User>> {
        return try {
            val response = httpClient.get("${AppConfig.API_BASE_URL}/api/v1/auth/me") {
                contentType(ContentType.Application.Json)
                header(HttpHeaders.Authorization, "Bearer $token")
            }.body<ApiRes<User>>()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}


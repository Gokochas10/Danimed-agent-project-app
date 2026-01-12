package com.danimed.agent_app.core.profile.infrastructure.api

import com.danimed.agent_app.core.profile.application.dto.res.ProfileResponse
import com.danimed.agent_app.shared.conf.AppConfig
import com.danimed.agent_app.shared.networks.NetworkError
import com.danimed.agent_app.shared.networks.dto.ApiRes
import com.danimed.agent_app.shared.networks.toNetworkError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class ProfileApi(private val httpClient: HttpClient) {
    suspend fun getProfile(): Result<ApiRes<ProfileResponse>> {
        return try {
            val response: HttpResponse = httpClient.get("${AppConfig.API_BASE_URL}/api/v1/auth/profile") {
                contentType(ContentType.Application.Json)
            }
            
            when (response.status) {
                HttpStatusCode.Unauthorized -> {
                    Result.failure(NetworkError.Unauthorized())
                }
                else -> {
                    val body = response.body<ApiRes<ProfileResponse>>()
                    Result.success(body)
                }
            }
        } catch (e: Exception) {
            Result.failure(e.toNetworkError())
        }
    }
}





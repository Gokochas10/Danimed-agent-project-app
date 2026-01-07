package com.danimed.agent_app.core.schedule.infrastructure.api

import com.danimed.agent_app.core.schedule.application.dto.res.ScheduleResponse
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

class ScheduleApi(private val httpClient: HttpClient) {
    suspend fun getSchedules(doctorId: Int): Result<ApiRes<List<ScheduleResponse>>> {
        return try {
            val response: HttpResponse = httpClient.get("${AppConfig.API_BASE_URL}/api/v1/schedules/doctor/$doctorId") {
                contentType(ContentType.Application.Json)
            }
            
            when (response.status) {
                HttpStatusCode.Unauthorized -> {
                    Result.failure(NetworkError.Unauthorized())
                }
                else -> {
                    val body = response.body<ApiRes<List<ScheduleResponse>>>()
                    Result.success(body)
                }
            }
        } catch (e: Exception) {
            Result.failure(e.toNetworkError())
        }
    }
}



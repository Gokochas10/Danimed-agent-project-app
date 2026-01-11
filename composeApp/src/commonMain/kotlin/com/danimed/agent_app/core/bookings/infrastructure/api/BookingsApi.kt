package com.danimed.agent_app.core.bookings.infrastructure.api

import com.danimed.agent_app.core.bookings.application.dto.res.BookingsResponseDto
import com.danimed.agent_app.core.bookings.application.dto.res.BookingEventDto
import com.danimed.agent_app.shared.conf.AppConfig
import com.danimed.agent_app.shared.networks.NetworkError
import com.danimed.agent_app.shared.networks.dto.ApiRes
import com.danimed.agent_app.shared.networks.toNetworkError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class BookingsApi(private val httpClient: HttpClient) {
    suspend fun getBookings(
        doctorId: Int, 
        date: String? = null,
        search: String? = null,
        page: Int = 1,
        limit: Int = 10
    ): Result<ApiRes<BookingsResponseDto>> {
        return try {
            val response: HttpResponse = httpClient.get("${AppConfig.API_BASE_URL}/api/v1/bookings/doctor/$doctorId") {
                contentType(ContentType.Application.Json)
                date?.let { parameter("date", it) }
                search?.let { parameter("p_search", it) }
                parameter("p_page", page)
                parameter("p_limit", limit)
            }
            
            when (response.status) {
                HttpStatusCode.Unauthorized -> {
                    Result.failure(NetworkError.Unauthorized())
                }
                else -> {
                    val body = response.body<ApiRes<BookingsResponseDto>>()
                    Result.success(body)
                }
            }
        } catch (e: Exception) {
            Result.failure(e.toNetworkError())
        }
    }

    suspend fun getBookingEvents(bookingId: Int): Result<ApiRes<List<BookingEventDto>>> {
        return try {
            val response: HttpResponse = httpClient.get("${AppConfig.API_BASE_URL}/api/v1/bookings/$bookingId/events") {
                contentType(ContentType.Application.Json)
            }

            when (response.status) {
                HttpStatusCode.Unauthorized -> {
                    Result.failure(NetworkError.Unauthorized())
                }
                else -> {
                    val body = response.body<ApiRes<List<BookingEventDto>>>()
                    Result.success(body)
                }
            }
        } catch (e: Exception) {
            Result.failure(e.toNetworkError())
        }
    }
}



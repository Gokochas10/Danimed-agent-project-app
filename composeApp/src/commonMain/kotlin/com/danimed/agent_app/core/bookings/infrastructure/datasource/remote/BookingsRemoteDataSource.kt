package com.danimed.agent_app.core.bookings.infrastructure.datasource.remote

import com.danimed.agent_app.core.bookings.application.dto.res.BookingsResponseDto
import com.danimed.agent_app.core.bookings.domain.model.BookingsResponse
import com.danimed.agent_app.core.bookings.domain.model.BookingEvent
import com.danimed.agent_app.core.bookings.infrastructure.api.BookingsApi
import com.danimed.agent_app.shared.networks.dto.ApiRes

class BookingsRemoteDataSource(private val bookingsApi: BookingsApi) {
    suspend fun getBookings(doctorId: Int, date: String? = null): Result<BookingsResponse> {
        return bookingsApi.getBookings(doctorId, date).fold(
            onSuccess = { apiRes ->
                if (apiRes.success && apiRes.data != null) {
                    Result.success(apiRes.data.toDomain())
                } else {
                    Result.failure(Exception(apiRes.message.content.joinToString(", ")))
                }
            },
            onFailure = { Result.failure(it) }
        )
    }

    suspend fun getBookingEvents(bookingId: Int): Result<List<BookingEvent>> {
        return bookingsApi.getBookingEvents(bookingId).fold(
            onSuccess = { apiRes ->
                if (apiRes.success && apiRes.data != null) {
                    Result.success(apiRes.data.map { it.toDomain() })
                } else {
                    Result.failure(Exception(apiRes.message.content.joinToString(", ")))
                }
            },
            onFailure = { Result.failure(it) }
        )
    }
}



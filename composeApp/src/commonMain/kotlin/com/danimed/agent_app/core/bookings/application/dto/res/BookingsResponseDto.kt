package com.danimed.agent_app.core.bookings.application.dto.res

import com.danimed.agent_app.core.bookings.domain.model.Booking
import com.danimed.agent_app.core.bookings.domain.model.BookingsResponse
import kotlinx.serialization.Serializable

@Serializable
data class BookingDto(
    val booking_id: Int,
    val patient_name: String,
    val booking_date: String,
    val start_time: String,
    val end_time: String,
    val duration_minutes: Int,
    val status: String? = null
) {
    fun toDomain(): Booking {
        return Booking(
            booking_id = booking_id,
            patient_name = patient_name,
            booking_date = booking_date,
            start_time = start_time,
            end_time = end_time,
            duration_minutes = duration_minutes,
            status = status
        )
    }
}

@Serializable
data class BookingsResponseDto(
    val bookings: List<BookingDto>,
    val server_date: String
) {
    fun toDomain(): BookingsResponse {
        return BookingsResponse(
            bookings = bookings.map { it.toDomain() },
            server_date = server_date
        )
    }
}



package com.danimed.agent_app.core.bookings.domain.repository

import com.danimed.agent_app.core.bookings.domain.model.BookingsResponse
import com.danimed.agent_app.core.bookings.domain.model.BookingEvent

interface BookingsRepository {
    suspend fun getBookings(doctorId: Int, date: String? = null): Result<BookingsResponse>
    suspend fun getBookingEvents(bookingId: Int): Result<List<BookingEvent>>
}



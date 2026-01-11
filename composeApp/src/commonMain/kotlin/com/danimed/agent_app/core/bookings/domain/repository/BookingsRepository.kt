package com.danimed.agent_app.core.bookings.domain.repository

import com.danimed.agent_app.core.bookings.domain.model.BookingsResponse
import com.danimed.agent_app.core.bookings.domain.model.BookingEvent

interface BookingsRepository {
    suspend fun getBookings(
        doctorId: Int, 
        date: String? = null,
        search: String? = null,
        page: Int = 1,
        limit: Int = 10
    ): Result<BookingsResponse>
    suspend fun getBookingEvents(bookingId: Int): Result<List<BookingEvent>>
}



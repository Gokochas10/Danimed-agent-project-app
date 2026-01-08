package com.danimed.agent_app.core.bookings.domain.usecase

import com.danimed.agent_app.core.bookings.domain.model.BookingEvent
import com.danimed.agent_app.core.bookings.domain.repository.BookingsRepository

class GetBookingEventsUseCase(
    private val bookingsRepository: BookingsRepository
) {
    suspend operator fun invoke(bookingId: Int): Result<List<BookingEvent>> {
        return bookingsRepository.getBookingEvents(bookingId)
    }
}


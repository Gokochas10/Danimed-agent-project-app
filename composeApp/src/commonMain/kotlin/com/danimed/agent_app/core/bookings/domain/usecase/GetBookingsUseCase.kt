package com.danimed.agent_app.core.bookings.domain.usecase

import com.danimed.agent_app.core.bookings.domain.model.BookingsResponse
import com.danimed.agent_app.core.bookings.domain.repository.BookingsRepository

class GetBookingsUseCase(
    private val bookingsRepository: BookingsRepository
) {
    suspend operator fun invoke(doctorId: Int, date: String? = null): Result<BookingsResponse> {
        return bookingsRepository.getBookings(doctorId, date)
    }
}



package com.danimed.agent_app.core.bookings.infrastructure.repository

import com.danimed.agent_app.core.bookings.domain.model.BookingsResponse
import com.danimed.agent_app.core.bookings.domain.model.BookingEvent
import com.danimed.agent_app.core.bookings.domain.repository.BookingsRepository
import com.danimed.agent_app.core.bookings.infrastructure.datasource.remote.BookingsRemoteDataSource

class BookingsRepositoryImpl(
    private val remoteDataSource: BookingsRemoteDataSource
) : BookingsRepository {
    override suspend fun getBookings(doctorId: Int, date: String?): Result<BookingsResponse> {
        return remoteDataSource.getBookings(doctorId, date)
    }

    override suspend fun getBookingEvents(bookingId: Int): Result<List<BookingEvent>> {
        return remoteDataSource.getBookingEvents(bookingId)
    }
}



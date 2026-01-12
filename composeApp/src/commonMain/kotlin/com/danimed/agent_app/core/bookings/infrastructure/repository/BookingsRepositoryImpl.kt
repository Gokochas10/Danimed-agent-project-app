package com.danimed.agent_app.core.bookings.infrastructure.repository

import com.danimed.agent_app.core.bookings.domain.model.BookingsResponse
import com.danimed.agent_app.core.bookings.domain.model.BookingEvent
import com.danimed.agent_app.core.bookings.domain.repository.BookingsRepository
import com.danimed.agent_app.core.bookings.infrastructure.datasource.remote.BookingsRemoteDataSource

class BookingsRepositoryImpl(
    private val remoteDataSource: BookingsRemoteDataSource
) : BookingsRepository {
    override suspend fun getBookings(
        doctorId: Int, 
        date: String?,
        search: String?,
        page: Int,
        limit: Int
    ): Result<BookingsResponse> {
        return remoteDataSource.getBookings(doctorId, date, search, page, limit)
    }

    override suspend fun getBookingEvents(bookingId: Int): Result<List<BookingEvent>> {
        return remoteDataSource.getBookingEvents(bookingId)
    }
}



package com.danimed.agent_app.shared.di

import com.danimed.agent_app.core.bookings.domain.repository.BookingsRepository
import com.danimed.agent_app.core.bookings.domain.usecase.GetBookingsUseCase
import com.danimed.agent_app.core.bookings.domain.usecase.GetBookingEventsUseCase
import com.danimed.agent_app.core.bookings.infrastructure.api.BookingsApi
import com.danimed.agent_app.core.bookings.infrastructure.datasource.remote.BookingsRemoteDataSource
import com.danimed.agent_app.core.bookings.infrastructure.repository.BookingsRepositoryImpl
import com.danimed.agent_app.shared.networks.createHttpClient

object BookingsModule {
    private val httpClient = createHttpClient()
    private val bookingsApi = BookingsApi(httpClient)
    private val bookingsRemoteDataSource = BookingsRemoteDataSource(bookingsApi)
    val bookingsRepository: BookingsRepository = BookingsRepositoryImpl(bookingsRemoteDataSource)
    val getBookingsUseCase = GetBookingsUseCase(bookingsRepository)
    val getBookingEventsUseCase = GetBookingEventsUseCase(bookingsRepository)
}



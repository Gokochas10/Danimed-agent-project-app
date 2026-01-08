package com.danimed.agent_app.core.bookings.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Booking(
    val booking_id: Int,
    val patient_name: String,
    val booking_date: String, // Format: "2026-01-07"
    val start_time: String, // Format: "10:00:00"
    val end_time: String, // Format: "12:00:00"
    val duration_minutes: Int,
    val status: String? = null
)

@Serializable
data class BookingsResponse(
    val bookings: List<Booking>,
    val server_date: String // Format: "2026-01-07"
)



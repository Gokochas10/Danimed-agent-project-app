package com.danimed.agent_app.core.bookings.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class BookingEvent(
    val event_id: Int,
    val status: String,
    val reason: String? = null,
    val notes: String? = null,
    val created_at: String // Format: "2026-01-08T12:33:55.635390"
)


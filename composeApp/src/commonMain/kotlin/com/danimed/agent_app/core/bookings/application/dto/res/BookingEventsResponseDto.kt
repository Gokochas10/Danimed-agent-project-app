package com.danimed.agent_app.core.bookings.application.dto.res

import com.danimed.agent_app.core.bookings.domain.model.BookingEvent
import kotlinx.serialization.Serializable

@Serializable
data class BookingEventDto(
    val event_id: Int,
    val status: String,
    val reason: String? = null,
    val notes: String? = null,
    val created_at: String
) {
    fun toDomain(): BookingEvent {
        return BookingEvent(
            event_id = event_id,
            status = status,
            reason = reason,
            notes = notes,
            created_at = created_at
        )
    }
}


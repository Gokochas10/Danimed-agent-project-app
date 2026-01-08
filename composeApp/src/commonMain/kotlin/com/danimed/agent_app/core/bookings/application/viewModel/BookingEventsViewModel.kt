package com.danimed.agent_app.core.bookings.application.viewModel

import androidx.compose.runtime.*
import com.danimed.agent_app.core.bookings.domain.model.BookingEvent
import com.danimed.agent_app.core.bookings.domain.usecase.GetBookingEventsUseCase
import com.danimed.agent_app.shared.networks.NetworkError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class BookingEventsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val events: List<BookingEvent> = emptyList()
)

class BookingEventsViewModel(
    private val getBookingEventsUseCase: GetBookingEventsUseCase
) {
    private val _uiState = mutableStateOf(BookingEventsUiState())
    val uiState: State<BookingEventsUiState> = _uiState

    fun loadEvents(bookingId: Int) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        CoroutineScope(Dispatchers.Default).launch {
            getBookingEventsUseCase(bookingId)
                .onSuccess { events ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        events = events,
                        error = null
                    )
                }
                .onFailure { exception ->
                    val isNetworkError = exception is NetworkError.NoConnection
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = if (isNetworkError) null else (exception.message ?: "Error al cargar eventos"),
                        events = emptyList()
                    )
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}


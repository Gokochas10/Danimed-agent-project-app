package com.danimed.agent_app.core.bookings.application.viewModel

import androidx.compose.runtime.*
import com.danimed.agent_app.core.bookings.domain.model.BookingsResponse
import com.danimed.agent_app.core.bookings.domain.usecase.GetBookingsUseCase
import com.danimed.agent_app.shared.networks.NetworkError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class BookingsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val bookingsResponse: BookingsResponse? = null
)

class BookingsViewModel(
    private val getBookingsUseCase: GetBookingsUseCase
) {
    private val _uiState = mutableStateOf(BookingsUiState())
    val uiState: State<BookingsUiState> = _uiState

    fun loadBookings(doctorId: Int, date: String? = null) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        
        CoroutineScope(Dispatchers.Default).launch {
            getBookingsUseCase(doctorId, date)
                .onSuccess { bookingsResponse ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        bookingsResponse = bookingsResponse,
                        error = null
                    )
                }
                .onFailure { exception ->
                    val isNetworkError = exception is NetworkError.NoConnection
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = if (isNetworkError) null else (exception.message ?: "Error al cargar citas"),
                        bookingsResponse = null
                    )
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}




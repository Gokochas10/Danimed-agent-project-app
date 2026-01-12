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
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val bookingsResponse: BookingsResponse? = null,
    val allBookings: List<com.danimed.agent_app.core.bookings.domain.model.Booking> = emptyList(),
    val currentPage: Int = 1,
    val hasMorePages: Boolean = false,
    val serverDate: String? = null
)

class BookingsViewModel(
    private val getBookingsUseCase: GetBookingsUseCase
) {
    private val _uiState = mutableStateOf(BookingsUiState())
    val uiState: State<BookingsUiState> = _uiState

    fun loadBookings(
        doctorId: Int, 
        date: String? = null,
        search: String? = null,
        page: Int = 1,
        limit: Int = 10,
        append: Boolean = false
    ) {
        if (append) {
            _uiState.value = _uiState.value.copy(isLoadingMore = true, error = null)
        } else {
            _uiState.value = _uiState.value.copy(
                isLoading = true, 
                error = null,
                allBookings = emptyList(),
                currentPage = 1,
                hasMorePages = false
            )
        }
        
        CoroutineScope(Dispatchers.Default).launch {
            getBookingsUseCase(doctorId, date, search, page, limit)
                .onSuccess { bookingsResponse ->
                    val newBookings = if (append) {
                        _uiState.value.allBookings + bookingsResponse.bookings
                    } else {
                        bookingsResponse.bookings
                    }
                    
                    // Si la respuesta tiene exactamente el límite de items, probablemente hay más páginas
                    val hasMore = bookingsResponse.bookings.size == limit
                    
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoadingMore = false,
                        bookingsResponse = bookingsResponse.copy(bookings = newBookings),
                        allBookings = newBookings,
                        currentPage = page,
                        hasMorePages = hasMore,
                        serverDate = bookingsResponse.server_date,
                        error = null
                    )
                }
                .onFailure { exception ->
                    val isNetworkError = exception is NetworkError.NoConnection
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoadingMore = false,
                        error = if (isNetworkError) null else (exception.message ?: "Error al cargar citas"),
                        bookingsResponse = if (append) _uiState.value.bookingsResponse else null
                    )
                }
        }
    }
    
    fun loadMoreBookings(
        doctorId: Int,
        date: String? = null,
        search: String? = null,
        limit: Int = 10
    ) {
        if (_uiState.value.isLoadingMore || !_uiState.value.hasMorePages) return
        
        val nextPage = _uiState.value.currentPage + 1
        loadBookings(doctorId, date, search, nextPage, limit, append = true)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}




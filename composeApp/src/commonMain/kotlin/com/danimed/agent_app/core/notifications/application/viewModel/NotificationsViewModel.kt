package com.danimed.agent_app.core.notifications.application.viewModel

import androidx.compose.runtime.*
import com.danimed.agent_app.core.notifications.domain.model.Notification
import com.danimed.agent_app.core.notifications.domain.usecase.GetNotificationsUseCase
import com.danimed.agent_app.core.notifications.domain.usecase.GetUnreadCountUseCase
import com.danimed.agent_app.core.notifications.domain.usecase.MarkAllAsReadUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class NotificationsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val notifications: List<Notification> = emptyList(),
    val unreadCount: Int = 0
)

class NotificationsViewModel(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val getUnreadCountUseCase: GetUnreadCountUseCase,
    private val markAllAsReadUseCase: MarkAllAsReadUseCase
) {
    private val _uiState = mutableStateOf(NotificationsUiState())
    val uiState: State<NotificationsUiState> = _uiState

    fun loadNotifications() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        
        CoroutineScope(Dispatchers.Default).launch {
            getNotificationsUseCase()
                .onSuccess { notifications ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        notifications = notifications,
                        error = null
                    )
                    loadUnreadCount()
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Error al cargar notificaciones"
                    )
                }
        }
    }
    
    fun loadUnreadCount() {
        CoroutineScope(Dispatchers.Default).launch {
            getUnreadCountUseCase()
                .onSuccess { count ->
                    _uiState.value = _uiState.value.copy(unreadCount = count)
                }
                .onFailure {
                    // Silently fail, just keep current count
                }
        }
    }
    
    fun markAllAsRead() {
        CoroutineScope(Dispatchers.Default).launch {
            markAllAsReadUseCase()
                .onSuccess {
                    // Reload notifications and count
                    loadNotifications()
                }
                .onFailure {
                    // Silently fail
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

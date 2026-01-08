package com.danimed.agent_app.core.scheduling.presentation.application.viewModel

import androidx.compose.runtime.*
import com.danimed.agent_app.core.scheduling.presentation.domain.model.Schedule
import com.danimed.agent_app.core.scheduling.presentation.domain.usecase.GetSchedulesUseCase
import com.danimed.agent_app.shared.networks.NetworkError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class ScheduleUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val schedules: List<com.danimed.agent_app.core.scheduling.presentation.domain.model.Schedule> = emptyList()
)

class ScheduleViewModel(
    private val getSchedulesUseCase: com.danimed.agent_app.core.scheduling.presentation.domain.usecase.GetSchedulesUseCase
) {
    private val _uiState = mutableStateOf(_root_ide_package_.com.danimed.agent_app.core.scheduling.presentation.application.viewModel.ScheduleUiState())
    val uiState: State<com.danimed.agent_app.core.scheduling.presentation.application.viewModel.ScheduleUiState> = _uiState

    fun loadSchedules(doctorId: Int) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        
        CoroutineScope(Dispatchers.Default).launch {
            getSchedulesUseCase(doctorId)
                .onSuccess { schedules ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        schedules = schedules,
                        error = null
                    )
                }
                .onFailure { exception ->
                    val isNetworkError = exception is NetworkError.NoConnection
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = if (isNetworkError) null else (exception.message ?: "Error al cargar horarios"),
                        schedules = emptyList()
                    )
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}


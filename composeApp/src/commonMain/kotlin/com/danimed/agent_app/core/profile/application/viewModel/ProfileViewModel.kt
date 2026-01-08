package com.danimed.agent_app.core.profile.application.viewModel

import androidx.compose.runtime.*
import com.danimed.agent_app.core.profile.domain.model.Profile
import com.danimed.agent_app.core.profile.domain.usecase.GetProfileUseCase
import com.danimed.agent_app.shared.networks.NetworkError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val profile: Profile? = null
)

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase
) {
    private val _uiState = mutableStateOf(ProfileUiState())
    val uiState: State<ProfileUiState> = _uiState

    fun loadProfile() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        
        CoroutineScope(Dispatchers.Default).launch {
            getProfileUseCase()
                .onSuccess { profile ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        profile = profile,
                        error = null
                    )
                }
                .onFailure { exception ->
                    val isNetworkError = exception is NetworkError.NoConnection
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = if (isNetworkError) null else (exception.message ?: "Error al cargar perfil"),
                        profile = null
                    )
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}




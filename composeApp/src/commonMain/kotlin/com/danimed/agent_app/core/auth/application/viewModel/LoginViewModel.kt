package com.danimed.agent_app.core.auth.application.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danimed.agent_app.core.auth.domain.usecase.LoginUseCase
import kotlinx.coroutines.launch

data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val token: String? = null
)

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    var uiState by mutableStateOf(LoginUiState())
        private set

    fun login(username: String, password: String, onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null, isSuccess = false, token = null)
            
            loginUseCase(username, password)
                .onSuccess { token ->
                    uiState = uiState.copy(isLoading = false, isSuccess = true, token = token)
                    onSuccess(token)
                }
                .onFailure { exception ->
                    uiState = uiState.copy(
                        isLoading = false,
                        error = exception.message ?: "Error al iniciar sesión",
                        isSuccess = false,
                        token = null
                    )
                }
        }
    }

    fun clearError() {
        uiState = uiState.copy(error = null)
    }
}


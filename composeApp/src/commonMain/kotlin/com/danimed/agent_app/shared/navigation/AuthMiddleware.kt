package com.danimed.agent_app.shared.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.danimed.agent_app.core.auth.presentation.screens.LoginScreen
import com.danimed.agent_app.core.auth.presentation.screens.SplashScreen
import com.danimed.agent_app.shared.di.AuthModule
import com.danimed.agent_app.shared.utils.TokenManagerProvider
import kotlinx.coroutines.delay

sealed class Screen {
    object Splash : Screen()
    object Login : Screen()
    object Home : Screen()
}

@Composable
fun AuthMiddleware(
    content: @Composable (Screen) -> Unit
) {
    val tokenManager = remember { TokenManagerProvider.getTokenManager() }
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Splash) }
    var isAuthenticated by remember { mutableStateOf<Boolean?>(null) }
    var showSplash by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(3000)
        showSplash = false
        val token = tokenManager.getToken()
        if (token != null) {
            val result = AuthModule.getCurrentUserUseCase(token)
            isAuthenticated = result.isSuccess
        } else {
            isAuthenticated = false
        }
    }

    LaunchedEffect(isAuthenticated, showSplash) {
        if (!showSplash) {
            when (isAuthenticated) {
                true -> currentScreen = Screen.Home
                false -> currentScreen = Screen.Login
                null -> currentScreen = Screen.Login
            }
        }
    }

    when {
        showSplash -> SplashScreen()
        currentScreen is Screen.Login -> LoginScreen(
            onLoginSuccess = { token ->
                tokenManager.saveToken(token)
                isAuthenticated = true
            }
        )
        currentScreen is Screen.Home -> {
            content(Screen.Home)
        }
        else -> SplashScreen()
    }
}


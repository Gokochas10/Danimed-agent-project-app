package com.danimed.agent_app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.danimed.agent_app.core.auth.presentation.screens.LoginScreen
import com.danimed.agent_app.core.auth.presentation.screens.SplashScreen
import com.danimed.agent_app.core.scheduling.presentation.screens.HomeScreen
import com.danimed.agent_app.shared.components.BottomNavItem
import com.danimed.agent_app.shared.navigation.AuthRedirectHandler
import com.danimed.agent_app.shared.theme.AppTheme
import com.danimed.agent_app.shared.utils.SplashState
import com.danimed.agent_app.shared.utils.TokenManagerProvider

sealed class AppScreen {
    object Splash : AppScreen()
    object Login : AppScreen()
    object Home : AppScreen()
}

@Composable
@Preview
fun App() {
    AppTheme {
        val tokenManager = remember { TokenManagerProvider.getTokenManager() }
        
        val initialScreen = remember {
            if (SplashState.hasShownSplash()) {
                val token = tokenManager.getToken()
                if (token != null) AppScreen.Home else AppScreen.Login
            } else {
                AppScreen.Splash
            }
        }
        
        var currentScreen by remember { mutableStateOf<AppScreen>(initialScreen) }
        var splashShown by remember { mutableStateOf(SplashState.hasShownSplash()) }
        
        DisposableEffect(Unit) {
            AuthRedirectHandler.setOnUnauthorizedCallback {
                tokenManager.clearToken()
                currentScreen = AppScreen.Login
            }
            
            onDispose {
                AuthRedirectHandler.clearCallback()
            }
        }
        
        when {
            !splashShown -> SplashScreen(
                onNavigateToHome = { 
                    splashShown = true
                    currentScreen = AppScreen.Home 
                },
                onNavigateToLogin = { 
                    splashShown = true
                    currentScreen = AppScreen.Login 
                }
            )
            currentScreen is AppScreen.Login -> LoginScreen(
                onLoginSuccess = { token ->
                    tokenManager.saveToken(token)
                    currentScreen = AppScreen.Home
                }
            )
            currentScreen is AppScreen.Home -> {
                var currentNavItem by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Agenda) }
                HomeScreen(
                    currentNavItem = currentNavItem,
                    onNavItemClick = { currentNavItem = it }
                )
            }
        }
    }
}
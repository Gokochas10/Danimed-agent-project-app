package com.danimed.agent_app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.danimed.agent_app.core.auth.presentation.screens.LoginScreen
import com.danimed.agent_app.core.auth.presentation.screens.SplashScreen
import com.danimed.agent_app.core.scheduling.presentation.screens.HomeScreen
import com.danimed.agent_app.shared.components.BottomNavItem
import com.danimed.agent_app.shared.components.NoInternetScreen
import com.danimed.agent_app.shared.components.VideoPreloader
import com.danimed.agent_app.shared.networks.NetworkErrorHandler
import com.danimed.agent_app.shared.navigation.AuthRedirectHandler
import com.danimed.agent_app.shared.theme.AppTheme
import com.danimed.agent_app.shared.utils.SplashState
import com.danimed.agent_app.shared.utils.TokenManagerProvider
import io.ktor.client.HttpClient
import io.ktor.client.request.head
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.jetbrains.compose.ui.tooling.preview.Preview

// Función expect para precargar video (solo implementada en Android)
expect suspend fun preloadVideoIfNeeded(resource: String)



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
        val httpClient = remember { HttpClient() }

        // La precarga del video se hace en MainActivity (Android) o equivalente en iOS

        // Observar el estado de conexión a internet
        val hasNoInternet by NetworkErrorHandler.hasNoInternet.collectAsState()

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
        var videoKey by remember { mutableIntStateOf(0) }
        var lastNoInternetState by remember { mutableStateOf(false) }
        var lastScreen by remember { mutableStateOf<AppScreen?>(null) }

        // Incrementar videoKey cada vez que aparece la pantalla de no internet
        // Esto fuerza la recarga del video cuando se muestra nuevamente
        // Se incrementa cuando:
        // 1. Cambia de no mostrarse a mostrarse
        // 2. Se muestra desde LoginScreen (incluso si ya estaba mostrándose)
        LaunchedEffect(hasNoInternet, splashShown, currentScreen) {
            val shouldShowNoInternet = hasNoInternet && splashShown
            val screenChanged = lastScreen != currentScreen
            
            if (shouldShowNoInternet) {
                // Incrementar si:
                // - Cambió de no mostrarse a mostrarse, O
                // - Se está mostrando desde LoginScreen (screenChanged y currentScreen es Login)
                if (!lastNoInternetState || (screenChanged && currentScreen is AppScreen.Login)) {
                    videoKey++
                }
                lastNoInternetState = true
            } else {
                lastNoInternetState = false
            }
            
            lastScreen = currentScreen
        }

        DisposableEffect(Unit) {
            AuthRedirectHandler.setOnUnauthorizedCallback {
                tokenManager.clearToken()
                currentScreen = AppScreen.Login
            }

            onDispose {
                AuthRedirectHandler.clearCallback()
                httpClient.close()
            }
        }

        // Mostrar pantalla de no internet si no hay conexión
        if (hasNoInternet && splashShown) {
            NoInternetScreen(
                onRetry = {
                    // Limpiar el estado de no internet
                    NetworkErrorHandler.clearNoInternet()
                },
                checkInternetConnection = {
                    // Verificar conexión a internet usando Ktor
                    withContext(Dispatchers.IO) {
                        try {
                            val response: HttpResponse = httpClient.head("https://www.google.com")
                            response.status == HttpStatusCode.OK
                        } catch (e: Exception) {
                            false
                        }
                    }
                },
                videoKey = videoKey
            )
        } else {
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
}
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
import com.danimed.agent_app.getPlatform
import com.danimed.agent_app.shared.components.BottomNavItem
import com.danimed.agent_app.shared.components.NoInternetScreen
import com.danimed.agent_app.shared.networks.NetworkErrorHandler
import com.danimed.agent_app.shared.di.AuthModule
import com.danimed.agent_app.shared.navigation.AuthRedirectHandler
import com.danimed.agent_app.shared.theme.AppTheme
import com.danimed.agent_app.shared.utils.CredentialsManagerProvider
import com.danimed.agent_app.shared.utils.FcmTokenManagerProvider
import com.danimed.agent_app.shared.utils.SplashState
import com.danimed.agent_app.shared.utils.TokenManagerProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import io.ktor.client.HttpClient
import io.ktor.client.request.head
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
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
        var isManualLogout by remember { mutableStateOf(false) }

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

        val credentialsManager = remember { CredentialsManagerProvider.getCredentialsManager() }
        val fcmTokenManager = remember { FcmTokenManagerProvider.getFcmTokenManager() }
        val platform = remember { 
            val platformName = getPlatform().name.lowercase()
            if (platformName.contains("android")) "android" else "ios"
        }
        var isRelogging by remember { mutableStateOf(false) }
        
        // Manejar re-login automático cuando el token expire
        LaunchedEffect(Unit) {
            // Este efecto se ejecuta cuando hay un cambio de estado que requiere re-login
        }
        
        DisposableEffect(Unit) {
            AuthRedirectHandler.setOnUnauthorizedCallback {
                // No intentar re-login automático si es un logout manual
                if (!isRelogging && !isManualLogout) {
                    isRelogging = true
                    // Intentar re-login automático si hay credenciales guardadas
                    val savedCredentials = credentialsManager.getCredentials()
                    if (savedCredentials != null) {
                        // Intentar re-login en background
                        CoroutineScope(Dispatchers.Default).launch {
                            val fcmToken = fcmTokenManager.getToken()
                            val result = AuthModule.loginUseCase(
                                savedCredentials.first,
                                savedCredentials.second,
                                fcmToken,
                                platform
                            )
                            result.onSuccess { newToken ->
                                tokenManager.saveToken(newToken)
                                isRelogging = false
                                // Notificar que el re-login fue exitoso
                                AuthRedirectHandler.attemptAutoRelogin(newToken)
                            }.onFailure {
                                // Si falla el re-login, limpiar credenciales y redirigir al login
                                tokenManager.clearToken()
                                credentialsManager.clearCredentials()
                                isRelogging = false
                                currentScreen = AppScreen.Login
                            }
                        }
                    } else {
                        // No hay credenciales guardadas, redirigir al login normalmente
                        tokenManager.clearToken()
                        isRelogging = false
                        currentScreen = AppScreen.Login
                    }
                }
            }
            
            AuthRedirectHandler.setOnAutoReloginCallback { newToken ->
                // El re-login fue exitoso, el token ya está guardado
                isRelogging = false
                // No necesitamos cambiar de pantalla, el usuario sigue en Home
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
                currentScreen is AppScreen.Login -> {
                    // Verificar si hay token y redirigir a Home, pero solo si NO es logout manual
                    LaunchedEffect(currentScreen, isManualLogout) {
                        if (currentScreen is AppScreen.Login) {
                            if (isManualLogout) {
                                // Es logout manual, asegurarse de que el token y credenciales estén limpios
                                tokenManager.clearToken()
                                credentialsManager.clearCredentials()
                                // Resetear el flag después de un pequeño delay para evitar loops
                                delay(100)
                                isManualLogout = false
                            } else {
                                // No es logout manual, verificar si hay token
                                val token = tokenManager.getToken()
                                if (token != null) {
                                    // Hay un token válido, redirigir a Home
                                    currentScreen = AppScreen.Home
                                }
                            }
                        }
                    }
                    
                    LoginScreen(
                        onLoginSuccess = { token ->
                            tokenManager.saveToken(token)
                            isManualLogout = false // Resetear flag al hacer login exitoso
                            currentScreen = AppScreen.Home
                        }
                    )
                }
                currentScreen is AppScreen.Home -> {
                    var currentNavItem by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Agenda) }
                    HomeScreen(
                        currentNavItem = currentNavItem,
                        onNavItemClick = { currentNavItem = it },
                        onLogout = {
                            // Limpiar token y credenciales ANTES de cambiar de pantalla
                            // Esto asegura que no se pueda hacer re-login automático
                            tokenManager.clearToken()
                            credentialsManager.clearCredentials()
                            // Marcar como logout manual y cambiar de pantalla
                            isManualLogout = true
                            isRelogging = false // Asegurar que no se intente re-login automático
                            currentScreen = AppScreen.Login
                        }
                    )
                }
            }
        }
    }
}
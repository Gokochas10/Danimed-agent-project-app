package com.danimed.agent_app.core.auth.presentation.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import agent_app.composeapp.generated.resources.Res
import agent_app.composeapp.generated.resources.danimed_logo
import com.danimed.agent_app.core.auth.presentation.components.TypewriterText
import com.danimed.agent_app.shared.di.AuthModule
import com.danimed.agent_app.shared.networks.NetworkError
import com.danimed.agent_app.shared.theme.PrimaryText
import com.danimed.agent_app.shared.theme.ScienceGothicFontFamily
import com.danimed.agent_app.shared.theme.SplashBackground
import com.danimed.agent_app.shared.utils.SplashState
import com.danimed.agent_app.shared.utils.TokenManagerProvider
import com.danimed.agent_app.shared.utils.CredentialsManagerProvider
import com.danimed.agent_app.shared.utils.FcmTokenManagerProvider
import com.danimed.agent_app.getPlatform
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {}
) {
    val fontFamily = ScienceGothicFontFamily()
    val density = LocalDensity.current
    val tokenManager = TokenManagerProvider.getTokenManager()

    // Animaciones para el icono
    val iconScale = remember { Animatable(0.3f) }
    val iconOffsetY = remember { Animatable(0f) }
    val iconAlpha = remember { Animatable(0f) }

    // Animación para el contenedor (texto)
    val containerOffsetY = with(density) { (-20).dp }
    val containerAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 800),
        label = "container_alpha"
    )

    // Animar el icono: aparece pequeño, crece y sube
    LaunchedEffect(Unit) {
        // 1. Aparecer con fade in
        iconAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 300, easing = EaseOutCubic)
        )

        // 2. Crecer del tamaño pequeño al completo
        iconScale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 600, easing = EaseOutCubic)
        )

        // 3. Subir un poco para dar espacio al texto
        iconOffsetY.animateTo(
            targetValue = -20f,
            animationSpec = tween(durationMillis = 400, easing = EaseOutCubic)
        )
    }

    LaunchedEffect(Unit) {
        if (!SplashState.hasShownSplash()) {
            delay(3000)
            SplashState.markSplashShown()
        }

        if (!SplashState.hasCheckedInitialAuth()) {
            SplashState.markInitialAuthChecked()
            val token = tokenManager.getToken()
            if (token != null) {
                val result = AuthModule.getCurrentUserUseCase(token)
                result.onSuccess {
                    onNavigateToHome()
                }.onFailure { exception ->
                    when (exception) {
                        is NetworkError.Unauthorized -> {
                            // Token expirado - intentar re-login automático si hay credenciales guardadas
                            val credentialsManager = CredentialsManagerProvider.getCredentialsManager()
                            val savedCredentials = credentialsManager.getCredentials()
                            
                            if (savedCredentials != null) {
                                // Intentar re-login automático
                                val fcmTokenManager = FcmTokenManagerProvider.getFcmTokenManager()
                                val platform = getPlatform().name.lowercase().let { 
                                    if (it.contains("android")) "android" else "ios" 
                                }
                                
                                val reloginResult = AuthModule.loginUseCase(
                                    savedCredentials.first,
                                    savedCredentials.second,
                                    fcmTokenManager.getToken(),
                                    platform
                                )
                                
                                reloginResult.onSuccess { newToken ->
                                    // Re-login exitoso, guardar nuevo token y navegar a Home
                                    tokenManager.saveToken(newToken)
                                    onNavigateToHome()
                                }.onFailure {
                                    // Re-login falló, limpiar todo y navegar al login
                                    tokenManager.clearToken()
                                    credentialsManager.clearCredentials()
                                    delay(1000)
                                    onNavigateToLogin()
                                }
                            } else {
                                // No hay credenciales guardadas, limpiar token y navegar al login
                                tokenManager.clearToken()
                                delay(1000)
                                onNavigateToLogin()
                            }
                        }
                        is NetworkError.NoConnection,
                        is NetworkError.ServerError,
                        is NetworkError.Unknown -> {
                            // Errores de red/servidor - no hacer nada, NetworkErrorHandler los maneja
                            // No navegar al login para que las pantallas de error se muestren
                        }
                        else -> {
                            // Otros errores - limpiar token y navegar al login
                            tokenManager.clearToken()
                            delay(1000)
                            onNavigateToLogin()
                        }
                    }
                }
            } else {
                delay(1000)
                onNavigateToLogin()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SplashBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .offset(y = containerOffsetY)
                .alpha(containerAlpha)
        ) {
            // Icono con animación independiente
            Image(
                painter = painterResource(Res.drawable.danimed_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .scale(iconScale.value)
                    .offset(y = with(density) { iconOffsetY.value.dp })
                    .alpha(iconAlpha.value)
            )

            Spacer(modifier = Modifier.height(24.dp))

            TypewriterText(
                text = "MEDSCHEDULER AI",
                color = PrimaryText,
                fontSize = 24.sp,
                fontFamily = fontFamily,
                textAlign = TextAlign.Center,
                delayMillis = 50,
                startDelay = 1300
            )

            Spacer(modifier = Modifier.height(16.dp))

            TypewriterText(
                text = "SISTEMA DE AGENDAMIENTO DE TURNOS",
                color = PrimaryText,
                fontSize = 17.sp,
                fontFamily = fontFamily,
                textAlign = TextAlign.Center,
                delayMillis = 50,
                startDelay = 2100 // Delay ajustado para que aparezca después de "MEDSCHEDULER AI"
            )
        }
    }
}
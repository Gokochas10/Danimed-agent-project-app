package com.danimed.agent_app.core.auth.presentation.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import agent_app.composeapp.generated.resources.danimed_logo_scheduler_white
import com.danimed.agent_app.core.auth.presentation.components.TypewriterText
import com.danimed.agent_app.shared.theme.PrimaryText
import com.danimed.agent_app.shared.theme.ScienceGothicFontFamily
import com.danimed.agent_app.shared.theme.SplashBackground

@Composable
fun SplashScreen() {
    val fontFamily = ScienceGothicFontFamily()
    val density = LocalDensity.current
    var startAnimation by remember { mutableStateOf(false) }
    
    val offsetY = with(density) { (-40).dp }
    
    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "alpha"
    )
    
    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.8f,
        animationSpec = tween(durationMillis = 800),
        label = "scale"
    )
    
    LaunchedEffect(Unit) {
        startAnimation = true
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
                .offset(y = offsetY)
                .alpha(alpha)
                .scale(scale)
        ) {
            Image(
                painter = painterResource(Res.drawable.danimed_logo_scheduler_white),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
            
            TypewriterText(
                text = "SISTEMA DE AGENDAMIENTO DE TURNOS",
                color = PrimaryText,
                fontSize = 20.sp,
                fontFamily = fontFamily,
                textAlign = TextAlign.Center,
                delayMillis = 50,
                startDelay = 800
            )
        }
    }
}


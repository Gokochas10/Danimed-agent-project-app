package com.danimed.agent_app.core.profile.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.statusBars
import com.danimed.agent_app.shared.theme.InterFontFamily
import com.danimed.agent_app.shared.theme.White

@Composable
fun ProfileHeader(
    alpha: Float = 1f,
    modifier: Modifier = Modifier
) {
    // Animar el alpha suavemente
    val animatedAlpha by animateFloatAsState(
        targetValue = alpha,
        animationSpec = tween(durationMillis = 400),
        label = "header_alpha"
    )
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(horizontal = 8.dp, vertical = 0.dp)
            .alpha(animatedAlpha)
            .graphicsLayer {
                // También animar la posición vertical para un efecto más suave
                translationY = (1f - animatedAlpha) * -20f
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Mi Perfil",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = InterFontFamily(),
                color = White,
                modifier = Modifier.padding(top = 20.dp)
            )
        }
    }
}





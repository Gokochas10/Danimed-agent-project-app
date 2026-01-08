package com.danimed.agent_app.core.scheduling.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Search
import compose.icons.feathericons.Bell
import androidx.compose.foundation.layout.statusBars
import androidx.compose.ui.text.font.FontWeight
import com.danimed.agent_app.shared.theme.InterFontFamily
import com.danimed.agent_app.shared.theme.White

@Composable
fun AgendaHeader(
    doctorId: String = "1805263782",
    scheduleTitle: String = "Bienvenido Joshua!",
    onSearchClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    alpha: Float = 1f,
    modifier: Modifier = Modifier
) {
    // Animar el alpha suavemente
    val animatedAlpha by animateFloatAsState(
        targetValue = alpha,
        animationSpec = tween(durationMillis = 500),
        label = "header_alpha"
    )
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(horizontal = 8.dp, vertical = 4.dp)
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
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = scheduleTitle,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = InterFontFamily(),
                color = White
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                IconButton(
                    onClick = onSearchClick,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        painter = rememberVectorPainter(image = FeatherIcons.Search),
                        contentDescription = "Buscar",
                        tint = White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(24.dp)
                        .background(Color(0x55FFFFFF))
                )

                IconButton(
                    onClick = onNotificationClick,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        painter = rememberVectorPainter(image = FeatherIcons.Bell),
                        contentDescription = "Notificaciones",
                        tint = White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

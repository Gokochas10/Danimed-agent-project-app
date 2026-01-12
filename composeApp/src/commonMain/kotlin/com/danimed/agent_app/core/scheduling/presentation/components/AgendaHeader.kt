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
import androidx.compose.foundation.clickable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Search
import compose.icons.feathericons.Bell
import androidx.compose.foundation.layout.statusBars
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.danimed.agent_app.shared.theme.InterFontFamily
import com.danimed.agent_app.shared.theme.White
import compose.icons.feathericons.ChevronDown
import compose.icons.feathericons.X

@Composable
fun AgendaHeader(
    doctorId: String = "1805263782",
    scheduleTitle: String = "Bienvenido Joshua!",
    searchQuery: String? = null,
    onSearchClick: (Offset) -> Unit = { },
    onNotificationClick: () -> Unit = {},
    onClearSearch: () -> Unit = {},
    unreadCount: Int = 0,
    alpha: Float = 1f,
    modifier: Modifier = Modifier
) {
    var showClearSearchDialog by remember { mutableStateOf(false) }
    // Animar el alpha suavemente
    val animatedAlpha by animateFloatAsState(
        targetValue = alpha,
        animationSpec = tween(durationMillis = 500),
        label = "header_alpha"
    )
    
    val density = LocalDensity.current
    var searchButtonPosition by remember { mutableStateOf<Offset?>(null) }
    
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

            if (searchQuery != null) {
                SearchQueryHeader(
                    searchQuery = searchQuery,
                    onClick = { showClearSearchDialog = true }
                )
            } else {
                Text(
                    text = scheduleTitle,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = InterFontFamily(),
                    color = White
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .onGloballyPositioned { coordinates ->
                            val position = coordinates.positionInRoot()
                            val centerX = position.x + coordinates.size.width / 2f
                            val centerY = position.y + coordinates.size.height / 2f
                            searchButtonPosition = Offset(centerX, centerY)
                        }
                        .clickable {
                            searchButtonPosition?.let { position ->
                                onSearchClick(position)
                            }
                        },
                    contentAlignment = Alignment.Center
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

                Box(
                    modifier = Modifier.size(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = onNotificationClick,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            painter = rememberVectorPainter(image = FeatherIcons.Bell),
                            contentDescription = "Notificaciones",
                            tint = White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    
                    // Badge con contador de notificaciones no leídas
                    if (unreadCount > 0) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = (-4).dp, y = 4.dp) // Más abajo e izquierda
                                .size(17.dp)
                                .background(
                                    color = Color(0xFFFF4444),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (unreadCount > 99) "99+" else unreadCount.toString(),
                                fontSize = 11.sp, // Letra más grande
                                lineHeight = 17.sp,
                                fontFamily = InterFontFamily(),
                                fontWeight = FontWeight.Bold,
                                color = White
                            )
                        }
                    }
                }
            }
        }
    }
    
    // Dialog para quitar selección de búsqueda
    if (showClearSearchDialog) {
        ClearSearchDialog(
            onDismiss = { showClearSearchDialog = false },
            onClearSearch = {
                onClearSearch()
                showClearSearchDialog = false
            }
        )
    }
}

@Composable
private fun SearchQueryHeader(
    searchQuery: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = searchQuery,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = InterFontFamily(),
            color = White,
            maxLines = 1
        )
        Icon(
            painter = rememberVectorPainter(image = FeatherIcons.ChevronDown),
            contentDescription = "Ver opciones",
            tint = White,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun ClearSearchDialog(
    onDismiss: () -> Unit,
    onClearSearch: () -> Unit
) {
    androidx.compose.ui.window.Dialog(
        onDismissRequest = onDismiss,
        properties = androidx.compose.ui.window.DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            contentAlignment = Alignment.BottomCenter
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = true,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(durationMillis = 300, easing = EaseOutCubic)
                ) + fadeIn(
                    animationSpec = tween(durationMillis = 300)
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(durationMillis = 300)
                ) + fadeOut(
                    animationSpec = tween(durationMillis = 300)
                )
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = White
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Header con X para cerrar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Búsqueda activa",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = InterFontFamily(),
                                color = com.danimed.agent_app.shared.theme.PrimaryBlue
                            )
                            Icon(
                                painter = rememberVectorPainter(image = FeatherIcons.X),
                                contentDescription = "Cerrar",
                                tint = com.danimed.agent_app.shared.theme.PrimaryBlue,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable { onDismiss() }
                            )
                        }
                        
                        // Botón para quitar selección
                        Button(
                            onClick = onClearSearch,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = com.danimed.agent_app.shared.theme.PrimaryBlue
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Quitar selección",
                                fontSize = 16.sp,
                                fontFamily = InterFontFamily(),
                                fontWeight = FontWeight.SemiBold,
                                color = White
                            )
                        }
                    }
                }
            }
        }
    }
}

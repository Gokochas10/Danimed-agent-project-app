package com.danimed.agent_app.core.notifications.presentation.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import com.danimed.agent_app.core.notifications.application.viewModel.NotificationsViewModel
import com.danimed.agent_app.core.notifications.domain.model.Notification
import com.danimed.agent_app.shared.di.NotificationsModule
import com.danimed.agent_app.shared.theme.InterFontFamily
import com.danimed.agent_app.shared.theme.PrimaryBlue
import com.danimed.agent_app.shared.theme.SplashBackground
import com.danimed.agent_app.shared.theme.White
import com.danimed.agent_app.shared.utils.SetStatusBarColor
import com.revenuecat.placeholder.PlaceholderDefaults
import com.revenuecat.placeholder.placeholder
import compose.icons.FeatherIcons
import compose.icons.feathericons.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

@Composable
fun NotificationsScreen(
    onBack: () -> Unit = {}
) {
    SetStatusBarColor(PrimaryBlue)
    
    val viewModel = remember { 
        NotificationsViewModel(
            NotificationsModule.getNotificationsUseCase,
            NotificationsModule.getUnreadCountUseCase,
            NotificationsModule.markAllAsReadUseCase
        )
    }
    val uiState by viewModel.uiState
    
    var isMarkingAllAsRead by remember { mutableStateOf(false) }
    var unreadNotificationIds by remember { mutableStateOf<Set<String>>(emptySet()) }
    
    // Notificaciones locales para la animación
    var localNotifications by remember { mutableStateOf<List<Notification>>(emptyList()) }
    
    // Sincronizar notificaciones locales con el estado
    LaunchedEffect(uiState.notifications) {
        if (!isMarkingAllAsRead) {
            localNotifications = uiState.notifications
            unreadNotificationIds = uiState.notifications
                .filter { !it.isRead }
                .map { it.id }
                .toSet()
        }
    }
    
    LaunchedEffect(Unit) {
        viewModel.loadNotifications()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FC))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SplashBackground)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(
                                painter = rememberVectorPainter(image = FeatherIcons.ArrowLeft),
                                contentDescription = "Volver",
                                tint = White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Text(
                            text = "Notificaciones",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = InterFontFamily(),
                            color = White
                        )
                    }
                }
            }
            
            // Lista de notificaciones
            if (uiState.isLoading && uiState.notifications.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Cargando...",
                        fontSize = 16.sp,
                        fontFamily = InterFontFamily(),
                        color = Color(0xFF666666)
                    )
                }
            } else if (uiState.notifications.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            painter = rememberVectorPainter(image = FeatherIcons.Bell),
                            contentDescription = null,
                            tint = Color(0xFFCCCCCC),
                            modifier = Modifier.size(64.dp)
                        )
                        Text(
                            text = "No hay notificaciones",
                            fontSize = 18.sp,
                            fontFamily = InterFontFamily(),
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF666666)
                        )
                        Text(
                            text = "Las notificaciones aparecerán aquí",
                            fontSize = 14.sp,
                            fontFamily = InterFontFamily(),
                            color = Color(0xFF999999)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(
                        top = 16.dp,
                        bottom = 80.dp, // Espacio para el botón inferior
                        start = 16.dp,
                        end = 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = localNotifications.ifEmpty { uiState.notifications },
                        key = { it.id }
                    ) { notification ->
                        val shouldShow = !isMarkingAllAsRead || 
                                        !unreadNotificationIds.contains(notification.id) ||
                                        notification.isRead
                        
                        AnimatedVisibility(
                            visible = shouldShow,
                            exit = fadeOut(animationSpec = tween(300)) + 
                                   slideOutVertically(
                                       animationSpec = tween(300),
                                       targetOffsetY = { -it }
                                   ),
                            enter = fadeIn(animationSpec = tween(300)) + 
                                   slideInVertically(
                                       animationSpec = tween(300),
                                       initialOffsetY = { it / 2 }
                                   )
                        ) {
                            NotificationItem(
                                notification = notification,
                                isLoading = uiState.isLoading
                            )
                        }
                    }
                }
            }
        }
        
        // Botón estático en la parte inferior
        AnimatedVisibility(
            visible = uiState.notifications.isNotEmpty() && 
                     uiState.notifications.any { !it.isRead } &&
                     !isMarkingAllAsRead,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300)),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Button(
                onClick = {
                    // Guardar IDs de notificaciones no leídas para la animación
                    unreadNotificationIds = uiState.notifications
                        .filter { !it.isRead }
                        .map { it.id }
                        .toSet()
                    isMarkingAllAsRead = true
                    
                    // Esperar a que termine la animación antes de eliminar
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(350) // Tiempo de animación de salida
                        viewModel.markAllAsRead() // Esto eliminará todas las notificaciones
                        delay(200) // Pequeño delay para que se actualice el estado
                        isMarkingAllAsRead = false
                        // Limpiar notificaciones locales después de eliminar
                        localNotifications = emptyList()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Text(
                    text = "Marcar todas como leídas",
                    fontSize = 16.sp,
                    fontFamily = InterFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    color = White,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun NotificationItem(
    notification: Notification,
    isLoading: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isRead) White else Color(0xFFF0F7FF)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Indicador de no leída
            if (!notification.isRead) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            color = PrimaryBlue,
                            shape = RoundedCornerShape(4.dp)
                        )
                )
            } else {
                Spacer(modifier = Modifier.width(8.dp))
            }
            
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = notification.title,
                    fontSize = 16.sp,
                    fontFamily = InterFontFamily(),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333),
                    modifier = Modifier
                        .fillMaxWidth()
                        .placeholder(
                            enabled = isLoading,
                            shape = RoundedCornerShape(4.dp),
                            highlight = PlaceholderDefaults.shimmer
                        )
                )
                
                Text(
                    text = notification.message,
                    fontSize = 14.sp,
                    fontFamily = InterFontFamily(),
                    color = Color(0xFF666666),
                    modifier = Modifier
                        .fillMaxWidth()
                        .placeholder(
                            enabled = isLoading,
                            shape = RoundedCornerShape(4.dp),
                            highlight = PlaceholderDefaults.shimmer
                        )
                )
                
                Text(
                    text = formatTimestamp(notification.timestamp),
                    fontSize = 12.sp,
                    fontFamily = InterFontFamily(),
                    color = Color(0xFF999999),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
private fun formatTimestamp(timestamp: Long): String {
    val now = Clock.System.now()
    val notificationTime = Instant.fromEpochMilliseconds(timestamp)
    val diff = now - notificationTime
    
    return when {
        diff.inWholeSeconds < 60 -> "Hace unos segundos"
        diff.inWholeMinutes < 60 -> "Hace ${diff.inWholeMinutes} minuto${if (diff.inWholeMinutes > 1) "s" else ""}"
        diff.inWholeHours < 24 -> "Hace ${diff.inWholeHours} hora${if (diff.inWholeHours > 1) "s" else ""}"
        diff.inWholeDays < 7 -> "Hace ${diff.inWholeDays} día${if (diff.inWholeDays > 1) "s" else ""}"
        else -> {
            val localDateTime = notificationTime.toLocalDateTime(TimeZone.currentSystemDefault())
            "${localDateTime.dayOfMonth}/${localDateTime.monthNumber}/${localDateTime.year}"
        }
    }
}

package com.danimed.agent_app.core.profile.presentation.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Icon
import com.danimed.agent_app.core.profile.application.viewModel.ProfileViewModel
import com.danimed.agent_app.core.profile.presentation.components.ProfileHeader
import com.danimed.agent_app.core.scheduling.presentation.utils.rememberHeaderAlpha
import com.danimed.agent_app.shared.components.BottomNavBar
import com.danimed.agent_app.shared.components.BottomNavItem
import com.danimed.agent_app.shared.di.ProfileModule
import com.danimed.agent_app.shared.theme.InterFontFamily
import com.danimed.agent_app.shared.theme.PrimaryBlue
import com.danimed.agent_app.shared.theme.SplashBackground
import com.danimed.agent_app.shared.theme.White
import com.danimed.agent_app.shared.utils.SetStatusBarColor
import com.revenuecat.placeholder.PlaceholderDefaults
import com.revenuecat.placeholder.placeholder
import compose.icons.FeatherIcons
import compose.icons.feathericons.*

@Composable
fun ProfileScreen(
    currentNavItem: BottomNavItem = BottomNavItem.Schedule,
    onNavItemClick: (BottomNavItem) -> Unit = {}
) {
    SetStatusBarColor(PrimaryBlue)
    
    val viewModel = remember { ProfileViewModel(ProfileModule.getProfileUseCase) }
    val uiState by viewModel.uiState
    
    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }
    
    // Estado del scroll para detectar dirección y calcular visibilidad del header
    val listState = rememberLazyListState()
    val isEmpty = uiState.profile == null
    
    // Calcular alpha del header basado en el scroll
    val headerAlpha = rememberHeaderAlpha(
        listState = listState,
        isEmpty = isEmpty,
        threshold = 100
    )
    
    // Animar la altura del contenedor del header para que se colapse completamente
    val headerContainerHeight by animateDpAsState(
        targetValue = if (headerAlpha > 0.01f) {
            68.dp * headerAlpha
        } else {
            0.dp
        },
        animationSpec = tween(durationMillis = 200),
        label = "header_container_height"
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FC))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SplashBackground)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars)
                ) {
                    // Contenedor animado para el header que se colapsa completamente
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(headerContainerHeight)
                    ) {
                        if (headerAlpha > 0.01f) {
                            Column {
                                ProfileHeader(
                                    alpha = headerAlpha
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
            
            // Contenido del perfil
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(
                    top = 20.dp,
                    bottom = 100.dp
                )
            ) {
                item {
                    // Avatar y nombre
                    ProfileHeaderCard(
                        profile = uiState.profile,
                        isLoading = uiState.isLoading
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Información del perfil
                    ProfileInfoCard(
                        profile = uiState.profile,
                        isLoading = uiState.isLoading
                    )
                }
            }
        }
        
        BottomNavBar(
            currentRoute = currentNavItem,
            onItemClick = onNavItemClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun ProfileHeaderCard(
    profile: com.danimed.agent_app.core.profile.domain.model.Profile?,
    isLoading: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Avatar circular
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .placeholder(
                        enabled = isLoading,
                        shape = CircleShape,
                        highlight = PlaceholderDefaults.shimmer
                    )
                    .clip(CircleShape)
                    .background(SplashBackground),
                contentAlignment = Alignment.Center
            ) {
                if (!isLoading) {
                    Icon(
                        painter = rememberVectorPainter(image = FeatherIcons.User),
                        contentDescription = "Avatar",
                        tint = White,
                        modifier = Modifier.size(50.dp)
                    )
                }
            }
            
            // Nombre completo
            Text(
                text = profile?.full_name ?: "Usuario",
                fontSize = 24.sp,
                fontFamily = InterFontFamily(),
                fontWeight = FontWeight.Bold,
                color = SplashBackground,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier
                    .width(200.dp)
                    .placeholder(
                        enabled = isLoading,
                        shape = RoundedCornerShape(4.dp),
                        highlight = PlaceholderDefaults.shimmer
                    )
            )
            
            // Estado activo/inactivo
            if (!isLoading && profile != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(
                                if (profile.is_active) Color(0xFF4CAF50) else Color(0xFFF44336)
                            )
                    )
                    Text(
                        text = if (profile.is_active) "Activo" else "Inactivo",
                        fontSize = 14.sp,
                        fontFamily = InterFontFamily(),
                        color = if (profile.is_active) Color(0xFF4CAF50) else Color(0xFFF44336),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileInfoCard(
    profile: com.danimed.agent_app.core.profile.domain.model.Profile?,
    isLoading: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Información Personal",
                fontSize = 18.sp,
                fontFamily = InterFontFamily(),
                fontWeight = FontWeight.Bold,
                color = SplashBackground
            )
            
            // Username
            ProfileInfoRow(
                icon = FeatherIcons.User,
                label = "Usuario",
                value = profile?.username ?: "",
                isLoading = isLoading
            )
            
            // Email
            ProfileInfoRow(
                icon = FeatherIcons.Mail,
                label = "Correo Electrónico",
                value = profile?.email ?: "",
                isLoading = isLoading
            )
            
            // Phone
            ProfileInfoRow(
                icon = FeatherIcons.Phone,
                label = "Teléfono",
                value = profile?.phone ?: "",
                isLoading = isLoading
            )
            
            // Role
            ProfileInfoRow(
                icon = FeatherIcons.Shield,
                label = "Rol",
                value = profile?.role ?: "",
                isLoading = isLoading
            )
        }
    }
}

@Composable
private fun ProfileInfoRow(
    icon: ImageVector,
    label: String,
    value: String,
    isLoading: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(SplashBackground.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = rememberVectorPainter(image = icon),
                contentDescription = label,
                tint = SplashBackground,
                modifier = Modifier.size(20.dp)
            )
        }
        
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = label,
                fontSize = 12.sp,
                fontFamily = InterFontFamily(),
                color = Color(0xFF666666),
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value.ifEmpty { "No disponible" },
                fontSize = 16.sp,
                fontFamily = InterFontFamily(),
                color = Color(0xFF333333),
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .width(150.dp)
                    .placeholder(
                        enabled = isLoading,
                        shape = RoundedCornerShape(4.dp),
                        highlight = PlaceholderDefaults.shimmer
                    )
            )
        }
    }
}


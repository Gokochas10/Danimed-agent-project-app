package com.danimed.agent_app.shared.components

import agent_app.composeapp.generated.resources.Res
import agent_app.composeapp.generated.resources.doctor_icon
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Calendar
import compose.icons.feathericons.CheckSquare
import com.danimed.agent_app.shared.theme.InterFontFamily
import com.danimed.agent_app.shared.theme.SplashBackground
import com.danimed.agent_app.shared.theme.White
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource as resPainterResource

// Clase sellada para manejar tanto iconos como imágenes
sealed class NavIcon {
    data class Vector(val imageVector: ImageVector) : NavIcon()
    data class DrawableRes(val resource: DrawableResource) : NavIcon()
}

sealed class BottomNavItem(
    val label: String,
    val icon: NavIcon
) {
    object Agenda : BottomNavItem("Agenda", NavIcon.Vector(FeatherIcons.CheckSquare))
    object Calendar : BottomNavItem("Calendario", NavIcon.Vector(FeatherIcons.Calendar))
    object Schedule : BottomNavItem("Mi Perfil", NavIcon.DrawableRes(Res.drawable.doctor_icon))
}

@Composable
fun BottomNavBar(
    currentRoute: BottomNavItem,
    onItemClick: (BottomNavItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem.Agenda,
        BottomNavItem.Calendar,
        BottomNavItem.Schedule
    )

    val density = LocalDensity.current

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .background(SplashBackground)
            .navigationBarsPadding()
    ) {
        val screenWidth = maxWidth
        val screenHeight = maxHeight

        // Altura del navbar
        val navbarHeight = (screenHeight * 0.08f).coerceIn(56.dp, 70.dp)


        // Estado para la posición del indicador
        var selectedItemOffset by remember { mutableStateOf(0f) }
        val dotSize = 6.dp

        // Animación del offset del punto
        val animatedOffset by animateDpAsState(
            targetValue = with(density) { selectedItemOffset.toDp() },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
            label = "indicator_offset"
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(navbarHeight)
        ) {
            // Indicador animado (punto)
            Box(
                modifier = Modifier
                    .offset(x = animatedOffset)
                    .align(Alignment.BottomStart)
                    .padding(bottom = 12.dp)
                    .size(dotSize)
                    .background(
                        color = White,
                        shape = CircleShape
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    BottomNavItemAnimated(
                        item = item,
                        isSelected = currentRoute == item,
                        onClick = { onItemClick(item) },
                        onPositionCalculated = { centerX ->
                            if (currentRoute == item) {
                                // Calcular offset para centrar el punto
                                selectedItemOffset = centerX - (dotSize.value * density.density / 2f)
                            }
                        },
                        navbarHeight = navbarHeight,
                        screenWidth = screenWidth,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomNavItemAnimated(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    onPositionCalculated: (Float) -> Unit,
    navbarHeight: Dp,
    screenWidth: Dp,
    modifier: Modifier = Modifier
) {
    // Animaciones
    val iconScale by animateFloatAsState(
        targetValue = if (isSelected) 1.15f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "icon_scale"
    )

    val iconAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.6f,
        animationSpec = tween(durationMillis = 300),
        label = "icon_alpha"
    )

    // Tamaños responsivos
    val iconSize = (navbarHeight * 0.35f).coerceIn(22.dp, 28.dp)
    val textSize = (screenWidth.value * 0.032f).coerceIn(10f, 13f)

    Box(
        modifier = modifier
            .fillMaxHeight()
            .clickable(
                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                indication = null
            ) { onClick() }
            .onGloballyPositioned { coordinates ->
                val positionInParent = coordinates.positionInParent()
                val centerX = positionInParent.x + (coordinates.size.width / 2f)
                onPositionCalculated(centerX)
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            // Icono con escala animada
            Box(
                modifier = Modifier.scale(iconScale),
                contentAlignment = Alignment.Center
            ) {
                when (val navIcon = item.icon) {
                    is NavIcon.Vector -> {
                        Icon(
                            imageVector = navIcon.imageVector,
                            contentDescription = item.label,
                            modifier = Modifier.size(iconSize),
                            tint = White.copy(alpha = iconAlpha)
                        )
                    }
                    is NavIcon.DrawableRes -> {
                        Image(
                            painter = resPainterResource(navIcon.resource),
                            contentDescription = item.label,
                            modifier = Modifier.size(iconSize),
                            colorFilter = ColorFilter.tint(White.copy(alpha = iconAlpha))
                        )
                    }
                }
            }


            // Contenedor de altura fija para el texto
            Box(
                modifier = Modifier.height(16.dp),
                contentAlignment = Alignment.Center
            ) {
                // Texto visible SOLO cuando está SELECCIONADO
                this@Column.AnimatedVisibility(
                    visible = !isSelected, // 👈 AHORA el texto se muestra SOLO cuando NO está seleccionado
                    enter = fadeIn(animationSpec = tween(250)) + scaleIn(initialScale = 0.9f),
                    exit = fadeOut(animationSpec = tween(150)) + scaleOut(targetScale = 0.9f)
                ) {
                    Text(
                        text = item.label,
                        fontSize = textSize.sp*0.9,
                        fontFamily = InterFontFamily(),
                        fontWeight = FontWeight.Bold,
                        color = White,
                        maxLines = 1,
                        modifier = Modifier.offset(y = (-4).dp)
                    )
                }

            }
        }
    }
}
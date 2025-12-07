package com.danimed.agent_app.shared.components

import agent_app.composeapp.generated.resources.Res
import agent_app.composeapp.generated.resources.doctor_icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Calendar
import compose.icons.feathericons.CheckSquare
import compose.icons.feathericons.Settings
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

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(SplashBackground)
            .navigationBarsPadding()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            BottomNavItem(
                item = item,
                isSelected = currentRoute == item,
                onClick = { onItemClick(item) }
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val color = if (isSelected) White else Color(0xFFB0B0B0)
    val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal

    IconButton(
        onClick = onClick,
        modifier = Modifier
            .defaultMinSize(minWidth = 85.dp, minHeight = 85.dp)
            .size(85.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
        ) {
            // Renderizar según el tipo de icono
            when (val navIcon = item.icon) {
                is NavIcon.Vector -> {
                    Icon(
                        imageVector = navIcon.imageVector,
                        contentDescription = item.label,
                        modifier = Modifier.size(24.dp),
                        tint = color
                    )
                }
                is NavIcon.DrawableRes -> {
                    Image(
                        painter = resPainterResource(navIcon.resource),
                        contentDescription = item.label,
                        modifier = Modifier.size(24.dp),
                        colorFilter = ColorFilter.tint(color)
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = item.label,
                fontSize = 12.sp,
                fontFamily = InterFontFamily(),
                fontWeight = fontWeight,
                color = color
            )
        }
    }
}
package com.danimed.agent_app.shared.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import agent_app.composeapp.generated.resources.Res
import agent_app.composeapp.generated.resources.science_gothic_regular
import agent_app.composeapp.generated.resources.inter

val SplashBackground = Color(0xFF284276)
val PrimaryText = Color(0xFFFFFFFF)
val LoginBackground = Color(0xFFEFF4FF)
val PrimaryBlue = Color(0xFF284276)
val White = Color(0xFFFFFFFF)

@Composable
fun ScienceGothicFontFamily(): FontFamily {
    val font = Font(
        resource = Res.font.science_gothic_regular,
        weight = FontWeight.Normal,
        style = FontStyle.Normal
    )
    return remember(font) {
        FontFamily(font)
    }
}

@Composable
fun InterFontFamily(): FontFamily {
    val font = Font(
        resource = Res.font.inter,
        weight = FontWeight.Normal,
        style = FontStyle.Normal
    )
    return remember(font) {
        FontFamily(font)
    }
}

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    background = LoginBackground,
    surface = White,
    onPrimary = White,
    onBackground = PrimaryBlue,
    onSurface = PrimaryBlue
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}


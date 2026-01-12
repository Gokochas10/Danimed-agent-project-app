package com.danimed.agent_app.core.auth.presentation.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun TypewriterText(
    text: String,
    color: androidx.compose.ui.graphics.Color,
    fontSize: androidx.compose.ui.unit.TextUnit = 20.sp,
    fontFamily: FontFamily = FontFamily.Default,
    textAlign: TextAlign = TextAlign.Center,
    delayMillis: Long = 50,
    startDelay: Long = 0
) {
    var displayedText by remember { mutableStateOf("") }

    LaunchedEffect(text, delayMillis, startDelay) {
        if (startDelay > 0) {
            delay(startDelay)
        }
        displayedText = ""
        text.forEachIndexed { index, _ ->
            displayedText = text.substring(0, index + 1)
            delay(delayMillis)
        }
    }

    Text(
        text = displayedText,
        color = color,
        fontSize = fontSize,
        fontFamily = fontFamily,
        textAlign = textAlign
    )
}















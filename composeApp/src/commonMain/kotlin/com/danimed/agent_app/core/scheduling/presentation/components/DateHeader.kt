package com.danimed.agent_app.core.scheduling.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danimed.agent_app.core.scheduling.presentation.utils.DateFormatter
import com.danimed.agent_app.shared.theme.InterFontFamily
import com.danimed.agent_app.shared.theme.SplashBackground
import com.danimed.agent_app.shared.theme.White
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

@Composable
fun DateHeader(
    date: LocalDate,
    onDateClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val isToday = date == today

    val dayName = DateFormatter.formatDayOfWeekSpanish(date.dayOfWeek)
    val monthName = DateFormatter.formatMonthSpanish(date.month)
    val dayNumber = date.dayOfMonth

    val formattedDate = "$dayName, $dayNumber de $monthName de ${date.year}"

    Column(
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                val width = size.width
                val lineHeight = 8.dp.toPx() // Altura de las líneas que sobresalen
                val strokeWidth = 5.dp.toPx()

                // Borde superior blanco
                drawLine(
                    color = White,
                    start = Offset(0f, 0f),
                    end = Offset(width, 0f),
                    strokeWidth = 1.dp.toPx()
                )

                // Tres líneas verticales simulando espirales de calendario
                // Línea izquierda
                drawLine(
                    color = White,
                    start = Offset(width * 0.25f, -lineHeight),
                    end = Offset(width * 0.25f, size.height / 5),
                    strokeWidth = strokeWidth
                )

                // Línea central
                drawLine(
                    color = White,
                    start = Offset(width * 0.5f, -lineHeight),
                    end = Offset(width * 0.5f, size.height / 5),
                    strokeWidth = strokeWidth
                )

                // Línea derecha
                drawLine(
                    color = White,
                    start = Offset(width * 0.75f, -lineHeight),
                    end = Offset(width * 0.75f, size.height / 5),
                    strokeWidth = strokeWidth
                )
            }
            .clickable(onClick = onDateClick)
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = formattedDate,
            fontSize = 15.sp,
            fontFamily = InterFontFamily(),
            fontWeight = FontWeight.Bold,
            color = White,
            textAlign = TextAlign.Center
        )
    }
}
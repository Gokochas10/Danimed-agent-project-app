package com.danimed.agent_app.shared.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danimed.agent_app.core.bookings.domain.model.BookingEvent
import com.danimed.agent_app.shared.theme.InterFontFamily
import com.danimed.agent_app.shared.theme.SplashBackground
import compose.icons.FeatherIcons
import compose.icons.feathericons.*

@Composable
fun BookingEventCard(event: BookingEvent) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFFFF)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        painter = rememberVectorPainter(image = FeatherIcons.Info),
                        contentDescription = "Estado",
                        tint = Color(0xFFF44336),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = event.status,
                        fontSize = 12.sp,
                        fontFamily = InterFontFamily(),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF44336)
                    )
                }
                Text(
                    text = formatDateTime(event.created_at),
                    fontSize = 11.sp,
                    fontFamily = InterFontFamily(),
                    color = Color(0xFF666666)
                )
            }

            event.reason?.let { reason ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        painter = rememberVectorPainter(image = FeatherIcons.AlertCircle),
                        contentDescription = "Razón",
                        tint = SplashBackground,
                        modifier = Modifier.size(11.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = reason,
                        fontSize = 12.sp,
                        fontFamily = InterFontFamily(),
                        color = Color(0xFF333333),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            event.notes?.let { notes ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        painter = rememberVectorPainter(image = FeatherIcons.FileText),
                        contentDescription = "Notas",
                        tint = SplashBackground,
                        modifier = Modifier.size(11.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = notes,
                        fontSize = 11.sp,
                        fontFamily = InterFontFamily(),
                        color = Color(0xFF666666),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

private fun formatDateTime(dateTimeString: String): String {
    return try {
        // Parse ISO 8601 format: "2026-01-08T12:33:55.635390"
        val parts = dateTimeString.split("T")
        if (parts.size == 2) {
            val datePart = parts[0] // "2026-01-08"
            val timePart = parts[1].split(".")[0] // "12:33:55"
            val timeParts = timePart.split(":")
            if (timeParts.size >= 2) {
                val hour = timeParts[0].toInt()
                val minute = timeParts[1].toInt()
                val period = if (hour < 12) "am" else "pm"
                val displayHour = when {
                    hour == 0 -> 12
                    hour > 12 -> hour - 12
                    else -> hour
                }
                val formattedTime = String.format("%d:%02d%s", displayHour, minute, period)
                "$datePart $formattedTime"
            } else {
                dateTimeString
            }
        } else {
            dateTimeString
        }
    } catch (e: Exception) {
        dateTimeString
    }
}



package com.danimed.agent_app.core.scheduling.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.danimed.agent_app.core.scheduling.presentation.utils.DateFormatter
import com.danimed.agent_app.shared.theme.InterFontFamily
import com.danimed.agent_app.shared.theme.SplashBackground
import com.danimed.agent_app.shared.theme.White
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

@Composable
fun MonthHeader(
    month: LocalDate,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = DateFormatter.formatMonth(month.month, month.year),
            fontSize = 16.sp,
            fontFamily = InterFontFamily(),
            fontWeight = FontWeight.Bold,
            color = SplashBackground
        )
        
        Row {
            IconButton(onClick = onPreviousMonth) {
                Text(
                    text = "<",
                    fontSize = 16.sp,
                    color = SplashBackground,
                    fontWeight = FontWeight.Bold
                )
            }
            
            IconButton(onClick = onNextMonth) {
                Text(
                    text = ">",
                    fontSize = 16.sp,
                    color = SplashBackground,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

package com.danimed.agent_app.core.scheduling.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.danimed.agent_app.core.scheduling.presentation.utils.DateFormatter
import kotlinx.datetime.LocalDate

@Composable
fun CalendarCard(
    currentMonth: LocalDate,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onMonthChange: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEFF4FF)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            MonthHeader(
                month = currentMonth,
                onPreviousMonth = {
                    val previous = DateFormatter.getPreviousMonth(currentMonth.month, currentMonth.year)
                    onMonthChange(previous)
                },
                onNextMonth = {
                    val next = DateFormatter.getNextMonth(currentMonth.month, currentMonth.year)
                    onMonthChange(next)
                }
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            DaysOfWeekHeader()
            
            Spacer(modifier = Modifier.height(4.dp))
            
            CalendarGrid(
                month = currentMonth,
                selectedDate = selectedDate,
                onDateSelected = onDateSelected
            )
        }
    }
}

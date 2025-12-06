package com.danimed.agent_app.core.scheduling.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

@Composable
fun CalendarGrid(
    month: LocalDate,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val firstDayOfMonth = LocalDate(month.year, month.month, 1)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek
    val firstDayOfWeekValue = if (firstDayOfWeek == DayOfWeek.SUNDAY) 7 else firstDayOfWeek.ordinal + 1
    
    val daysInMonth = when (month.month) {
        Month.JANUARY, Month.MARCH, Month.MAY, Month.JULY, Month.AUGUST, Month.OCTOBER, Month.DECEMBER -> 31
        Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER -> 30
        Month.FEBRUARY -> {
            val isLeapYear = (month.year % 4 == 0 && month.year % 100 != 0) || (month.year % 400 == 0)
            if (isLeapYear) 29 else 28
        }
    }
    
    val days = mutableListOf<LocalDate?>()
    
    for (i in 1 until firstDayOfWeekValue) {
        days.add(null)
    }
    
    for (day in 1..daysInMonth) {
        days.add(LocalDate(month.year, month.month, day))
    }
    
    val remainingDays = 42 - days.size
    for (i in 1..remainingDays) {
        days.add(null)
    }
    
    val rows = days.chunked(7)
    
    Column(modifier = modifier) {
        rows.forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                week.forEach { date ->
                    CalendarDay(
                        date = date,
                        isSelected = date != null && date == selectedDate,
                        isCurrentMonth = date != null && date.month == month.month,
                        onClick = { if (date != null) onDateSelected(date) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

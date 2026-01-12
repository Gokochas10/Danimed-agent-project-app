package com.danimed.agent_app.core.scheduling.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danimed.agent_app.core.scheduling.presentation.utils.DateFormatter
import com.danimed.agent_app.shared.theme.InterFontFamily
import com.danimed.agent_app.shared.theme.SplashBackground
import com.danimed.agent_app.shared.theme.White
import com.danimed.agent_app.shared.utils.currentLocalDate
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

data class WeekDay(
    val date: LocalDate,
    val dayName: String,
    val dayNumber: Int,
    val isToday: Boolean,
    val isSelected: Boolean
)

@Composable
fun WeekSelector(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val today = currentLocalDate()
    
    val weekDays = remember(selectedDate) {
        // Obtener el primer día del mes de la fecha seleccionada
        val firstDayOfMonth = LocalDate(selectedDate.year, selectedDate.month, 1)
        
        // Obtener el último día del mes
        val lastDayOfMonth = getLastDayOfMonth(selectedDate.year, selectedDate.month)
        
        // Obtener el lunes de la semana que contiene el primer día del mes
        val startOfWeek = getStartOfWeek(firstDayOfMonth)
        
        // Obtener el domingo de la semana que contiene el último día del mes
        val endOfWeek = getEndOfWeek(lastDayOfMonth)
        
        // Calcular el número de días entre el lunes inicial y el domingo final
        val totalDays = getDaysBetween(startOfWeek, endOfWeek)
        
        // Generar todos los días del mes (desde el lunes inicial hasta el domingo final)
        (0..totalDays).map { dayOffset ->
            val date = addDays(startOfWeek, dayOffset)
            WeekDay(
                date = date,
                dayName = DateFormatter.formatDayOfWeek(date.dayOfWeek).take(3),
                dayNumber = date.dayOfMonth,
                isToday = date == today,
                isSelected = date == selectedDate
            )
        }
    }
    
    val listState = rememberLazyListState()
    val selectedIndex = weekDays.indexOfFirst { it.isSelected }
    
    LaunchedEffect(selectedIndex) {
        if (selectedIndex >= 0) {
            listState.animateScrollToItem(selectedIndex)
        }
    }
    
    LazyRow(
        state = listState,
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFF7F9FC))
            .padding(horizontal = 16.dp, vertical = 2.dp)
        ,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(weekDays) { index, weekDay ->
            WeekDayItem(
                weekDay = weekDay,
                onClick = { onDateSelected(weekDay.date) }
            )
        }
    }
}

@Composable
private fun WeekDayItem(
    weekDay: WeekDay,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(50.dp)
            .height(60.dp) // Reducido de 70.dp a 60.dp
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        // Background box that moves with scroll
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp) // Reducido de 70.dp a 60.dp
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (weekDay.isSelected) SplashBackground else Color.Transparent
                )
        )

        // Content
        Column(
            modifier = Modifier.padding(vertical = 6.dp), // Reducido de 8.dp a 6.dp
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = weekDay.dayName,
                fontSize = 12.sp,
                fontFamily = InterFontFamily(),
                color = if (weekDay.isSelected) White else Color(0xFF666666),
                fontWeight = if (weekDay.isSelected) FontWeight.Bold else FontWeight.Normal
            )

            Spacer(modifier = Modifier.height(1.dp))

            Text(
                text = weekDay.dayNumber.toString(),
                fontSize = 16.sp,
                fontFamily = InterFontFamily(),
                color = if (weekDay.isSelected) White else SplashBackground,
                fontWeight = if (weekDay.isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}
private fun getStartOfWeek(date: LocalDate): LocalDate {
    val dayOfWeek = date.dayOfWeek
    val daysToSubtract = when (dayOfWeek) {
        DayOfWeek.MONDAY -> 0
        DayOfWeek.TUESDAY -> 1
        DayOfWeek.WEDNESDAY -> 2
        DayOfWeek.THURSDAY -> 3
        DayOfWeek.FRIDAY -> 4
        DayOfWeek.SATURDAY -> 5
        DayOfWeek.SUNDAY -> 6
    }
    
    return subtractDays(date, daysToSubtract)
}

private fun getEndOfWeek(date: LocalDate): LocalDate {
    val dayOfWeek = date.dayOfWeek
    val daysToAdd = when (dayOfWeek) {
        DayOfWeek.MONDAY -> 6
        DayOfWeek.TUESDAY -> 5
        DayOfWeek.WEDNESDAY -> 4
        DayOfWeek.THURSDAY -> 3
        DayOfWeek.FRIDAY -> 2
        DayOfWeek.SATURDAY -> 1
        DayOfWeek.SUNDAY -> 0
    }
    
    return addDays(date, daysToAdd)
}

private fun getLastDayOfMonth(year: Int, month: kotlinx.datetime.Month): LocalDate {
    val daysInMonth = getDaysInMonth(month, year)
    return LocalDate(year, month, daysInMonth)
}

private fun getDaysBetween(startDate: LocalDate, endDate: LocalDate): Int {
    var count = 0
    var currentDate = startDate
    while (currentDate <= endDate) {
        count++
        if (currentDate < endDate) {
            currentDate = addDays(currentDate, 1)
        } else {
            break
        }
    }
    return count - 1 // -1 porque queremos el número de días entre, no incluyendo el último
}

private fun subtractDays(date: LocalDate, days: Int): LocalDate {
    var currentDate = date
    repeat(days) {
        currentDate = if (currentDate.dayOfMonth > 1) {
            LocalDate(currentDate.year, currentDate.month, currentDate.dayOfMonth - 1)
        } else {
            val previousMonth = if (currentDate.month.ordinal == 0) {
                kotlinx.datetime.Month.DECEMBER
            } else {
                kotlinx.datetime.Month.values()[currentDate.month.ordinal - 1]
            }
            val year = if (currentDate.month.ordinal == 0) currentDate.year - 1 else currentDate.year
            val daysInPreviousMonth = getDaysInMonth(previousMonth, year)
            LocalDate(year, previousMonth, daysInPreviousMonth)
        }
    }
    return currentDate
}

private fun addDays(date: LocalDate, days: Int): LocalDate {
    var currentDate = date
    repeat(days) {
        val daysInMonth = getDaysInMonth(currentDate.month, currentDate.year)
        currentDate = if (currentDate.dayOfMonth < daysInMonth) {
            LocalDate(currentDate.year, currentDate.month, currentDate.dayOfMonth + 1)
        } else {
            val nextMonth = if (currentDate.month.ordinal == 11) {
                kotlinx.datetime.Month.JANUARY
            } else {
                kotlinx.datetime.Month.values()[currentDate.month.ordinal + 1]
            }
            val year = if (currentDate.month.ordinal == 11) currentDate.year + 1 else currentDate.year
            LocalDate(year, nextMonth, 1)
        }
    }
    return currentDate
}

private fun getDaysInMonth(month: kotlinx.datetime.Month, year: Int): Int {
    return when (month) {
        kotlinx.datetime.Month.JANUARY, kotlinx.datetime.Month.MARCH, kotlinx.datetime.Month.MAY,
        kotlinx.datetime.Month.JULY, kotlinx.datetime.Month.AUGUST, kotlinx.datetime.Month.OCTOBER,
        kotlinx.datetime.Month.DECEMBER -> 31
        kotlinx.datetime.Month.APRIL, kotlinx.datetime.Month.JUNE, kotlinx.datetime.Month.SEPTEMBER,
        kotlinx.datetime.Month.NOVEMBER -> 30
        kotlinx.datetime.Month.FEBRUARY -> {
            val isLeapYear = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
            if (isLeapYear) 29 else 28
        }
    }
}


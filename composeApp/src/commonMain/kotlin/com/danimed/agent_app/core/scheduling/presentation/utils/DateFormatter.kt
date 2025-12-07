package com.danimed.agent_app.core.scheduling.presentation.utils

import kotlinx.datetime.Month
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

object DateFormatter {
    private val monthNames = mapOf(
        Month.JANUARY to "January",
        Month.FEBRUARY to "February",
        Month.MARCH to "March",
        Month.APRIL to "April",
        Month.MAY to "May",
        Month.JUNE to "June",
        Month.JULY to "July",
        Month.AUGUST to "August",
        Month.SEPTEMBER to "September",
        Month.OCTOBER to "October",
        Month.NOVEMBER to "November",
        Month.DECEMBER to "December"
    )
    
    private val monthNamesSpanish = mapOf(
        Month.JANUARY to "Enero",
        Month.FEBRUARY to "Febrero",
        Month.MARCH to "Marzo",
        Month.APRIL to "Abril",
        Month.MAY to "Mayo",
        Month.JUNE to "Junio",
        Month.JULY to "Julio",
        Month.AUGUST to "Agosto",
        Month.SEPTEMBER to "Septiembre",
        Month.OCTOBER to "Octubre",
        Month.NOVEMBER to "Noviembre",
        Month.DECEMBER to "Diciembre"
    )
    
    private val dayNamesSpanish = mapOf(
        DayOfWeek.MONDAY to "Lunes",
        DayOfWeek.TUESDAY to "Martes",
        DayOfWeek.WEDNESDAY to "Miércoles",
        DayOfWeek.THURSDAY to "Jueves",
        DayOfWeek.FRIDAY to "Viernes",
        DayOfWeek.SATURDAY to "Sábado",
        DayOfWeek.SUNDAY to "Domingo"
    )
    
    private val dayNames = mapOf(
        DayOfWeek.MONDAY to "Mon",
        DayOfWeek.TUESDAY to "Tue",
        DayOfWeek.WEDNESDAY to "Wed",
        DayOfWeek.THURSDAY to "Thu",
        DayOfWeek.FRIDAY to "Fri",
        DayOfWeek.SATURDAY to "Sat",
        DayOfWeek.SUNDAY to "Sun"
    )
    
    private val monthOrder = listOf(
        Month.JANUARY, Month.FEBRUARY, Month.MARCH, Month.APRIL,
        Month.MAY, Month.JUNE, Month.JULY, Month.AUGUST,
        Month.SEPTEMBER, Month.OCTOBER, Month.NOVEMBER, Month.DECEMBER
    )
    
    fun formatMonth(month: Month, year: Int): String {
        return "${monthNames[month]} $year"
    }
    
    fun formatDayOfWeek(dayOfWeek: DayOfWeek): String {
        return dayNames[dayOfWeek] ?: ""
    }
    
    fun formatDayOfWeekSpanish(dayOfWeek: DayOfWeek): String {
        return dayNamesSpanish[dayOfWeek] ?: ""
    }
    
    fun formatMonthSpanish(month: Month): String {
        return monthNamesSpanish[month] ?: ""
    }
    
    fun getPreviousMonth(month: Month, year: Int): LocalDate {
        val currentIndex = monthOrder.indexOf(month)
        return if (currentIndex == 0) {
            LocalDate(year - 1, Month.DECEMBER, 1)
        } else {
            LocalDate(year, monthOrder[currentIndex - 1], 1)
        }
    }
    
    fun getNextMonth(month: Month, year: Int): LocalDate {
        val currentIndex = monthOrder.indexOf(month)
        return if (currentIndex == 11) {
            LocalDate(year + 1, Month.JANUARY, 1)
        } else {
            LocalDate(year, monthOrder[currentIndex + 1], 1)
        }
    }
}


package com.danimed.agent_app.shared.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDate

actual fun currentLocalDate(): LocalDate {
    val now = NSDate()
    val calendar: NSCalendar = NSCalendar.currentCalendar()
    val components = calendar.components(
        NSCalendarUnitYear or NSCalendarUnitMonth or NSCalendarUnitDay,
        fromDate = now
    )

    val year = components.year!!.toInt()
    val monthNumber = components.month!!.toInt()
    val day = components.day!!.toInt()

    val month = Month(monthNumber)
    return LocalDate(year, month, day)
}






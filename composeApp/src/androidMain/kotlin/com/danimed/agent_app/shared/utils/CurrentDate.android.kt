package com.danimed.agent_app.shared.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

actual fun currentLocalDate(): LocalDate {
    return Clock.System.todayIn(TimeZone.currentSystemDefault())
}



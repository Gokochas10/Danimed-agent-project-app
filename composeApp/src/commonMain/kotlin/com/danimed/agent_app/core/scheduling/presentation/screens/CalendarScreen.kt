package com.danimed.agent_app.core.scheduling.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.danimed.agent_app.core.scheduling.presentation.components.CalendarCard
import com.danimed.agent_app.core.scheduling.presentation.components.CalendarTopBar
import com.danimed.agent_app.core.scheduling.presentation.components.EventListSection
import com.danimed.agent_app.core.scheduling.presentation.model.CalendarEvent
import com.danimed.agent_app.shared.components.BottomNavBar
import com.danimed.agent_app.shared.components.BottomNavItem
import com.danimed.agent_app.shared.theme.SplashBackground
import com.danimed.agent_app.shared.theme.White
import com.danimed.agent_app.shared.utils.SetStatusBarColor
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

@OptIn(kotlin.time.ExperimentalTime::class)
@Composable
fun CalendarScreen(
    currentNavItem: BottomNavItem = BottomNavItem.Calendar,
    onNavItemClick: (BottomNavItem) -> Unit = {}
) {
    SetStatusBarColor(SplashBackground)

    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    var selectedDate by remember { mutableStateOf(today) }
    var currentMonth by remember { mutableStateOf(today) }
    
    val events = remember(selectedDate) {
        listOf(
            CalendarEvent(
                id = "1",
                title = "Demo meeting 1",
                time = "9:30am - 10:00am",
                colorIndicator = Color(0xFF4FC3F7)
            ),
            CalendarEvent(
                id = "2",
                title = "Demo meeting 2",
                time = "9:30am - 10:00am",
                colorIndicator = Color(0xFFFFEB3B)
            )
        )
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            CalendarTopBar()
            
            CalendarCard(
                currentMonth = currentMonth,
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it },
                onMonthChange = { currentMonth = it }
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            EventListSection(
                selectedDate = selectedDate,
                events = events,
                modifier = Modifier.weight(1f)
            )
        }
        
        BottomNavBar(
            currentRoute = currentNavItem,
            onItemClick = onNavItemClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )
    }
}

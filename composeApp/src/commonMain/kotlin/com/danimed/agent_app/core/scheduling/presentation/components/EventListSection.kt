package com.danimed.agent_app.core.scheduling.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danimed.agent_app.core.scheduling.presentation.components.EventCard
import com.danimed.agent_app.core.scheduling.presentation.model.CalendarEvent
import com.danimed.agent_app.core.scheduling.presentation.utils.DateFormatter
import com.danimed.agent_app.shared.theme.InterFontFamily
import com.danimed.agent_app.shared.theme.SplashBackground
import com.danimed.agent_app.shared.theme.White
import kotlinx.datetime.LocalDate

@Composable
fun EventListSection(
    selectedDate: LocalDate,
    events: List<CalendarEvent>,
    modifier: Modifier = Modifier
) {
    val dayName = DateFormatter.formatDayOfWeek(selectedDate.dayOfWeek)
    val dayNumber = selectedDate.dayOfMonth
    
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .navigationBarsPadding(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            bottom = 100.dp
        )
    ) {
        item {
            SelectedDateHeader(
                dayName = dayName,
                dayNumber = dayNumber
            )
        }
        
        items(events) { event ->
            EventCard(event = event)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun SelectedDateHeader(
    dayName: String,
    dayNumber: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color(0xFFEFF4FF)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = dayNumber.toString(),
                fontSize = 14.sp,
                fontFamily = InterFontFamily(),
                fontWeight = FontWeight.Bold,
                color = SplashBackground
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Text(
            text = dayName,
            fontSize = 16.sp,
            fontFamily = InterFontFamily(),
            fontWeight = FontWeight.Bold,
            color = SplashBackground
        )
    }
}

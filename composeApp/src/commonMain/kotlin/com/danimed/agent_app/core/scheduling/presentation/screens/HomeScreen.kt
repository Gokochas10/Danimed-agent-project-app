package com.danimed.agent_app.core.scheduling.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import com.danimed.agent_app.core.scheduling.presentation.components.AgendaCard
import com.danimed.agent_app.core.scheduling.presentation.components.AgendaHeader
import com.danimed.agent_app.core.scheduling.presentation.components.DateHeader
import com.danimed.agent_app.core.scheduling.presentation.components.MonthYearPicker
import com.danimed.agent_app.core.scheduling.presentation.components.WeekSelector
import com.danimed.agent_app.core.scheduling.presentation.utils.rememberHeaderAlpha
import com.danimed.agent_app.shared.components.BottomNavBar
import com.danimed.agent_app.shared.components.BottomNavItem
import com.danimed.agent_app.shared.theme.InterFontFamily
import com.danimed.agent_app.shared.theme.PrimaryBlue
import com.danimed.agent_app.shared.theme.SplashBackground
import com.danimed.agent_app.shared.theme.White
import com.danimed.agent_app.shared.utils.SetStatusBarColor
import androidx.compose.ui.graphics.Color.Companion.Transparent
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

data class AgendaItem(
    val id: String,
    val title: String,
    val duration: String,
    val time: String,
    val colorIndicator: Color,
    val startTime: String,
    val endTime: String,
    val date: LocalDate
)

@Composable
fun HomeScreen(
    currentNavItem: BottomNavItem = BottomNavItem.Agenda,
    onNavItemClick: (BottomNavItem) -> Unit = {}
) {
    AgendaScreen(
        currentNavItem = currentNavItem,
        onNavItemClick = onNavItemClick
    )
}

@Composable
private fun AgendaScreen(
    currentNavItem: BottomNavItem = BottomNavItem.Agenda,
    onNavItemClick: (BottomNavItem) -> Unit = {}
) {
    SetStatusBarColor(PrimaryBlue)
    
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    var selectedDate by remember { mutableStateOf(today) }
    var showMonthYearPicker by remember { mutableStateOf(false) }
    
    val allAgendaItems = remember {
        listOf(
            AgendaItem(
                id = "1",
                title = "Juan Jose Fiallos",
                duration = "60 MIN",
                time = "10:30am - 11:30am",
                colorIndicator = Color(0xFF4FC3F7),
                startTime = "9:30am",
                endTime = "10:30am",
                date = today
            ),
            AgendaItem(
                id = "2",
                title = "Silvana Diaz",
                duration = "60 MIN",
                time = "9:30am - 10:00am",
                colorIndicator = Color(0xFFFFEB3B),
                startTime = "9:30am",
                endTime = "10:00am",
                date = today
            ),
            AgendaItem(
                id = "3",
                title = "Matias Gamboa",
                duration = "30 MIN",
                time = "9:30am - 10:00am",
                colorIndicator = Color(0xFFFFEB3B),
                startTime = "9:30am",
                endTime = "10:00am",
                date = today
            ),
            AgendaItem(
                id = "4",
                title = "Lenin Herrera",
                duration = "30 MIN",
                time = "9:30am - 10:00am",
                colorIndicator = Color(0xFFFFEB3B),
                startTime = "9:30am",
                endTime = "10:00am",
                date = today
            ),
            AgendaItem(
                id = "4",
                title = "Lenin Herrera",
                duration = "30 MIN",
                time = "9:30am - 10:00am",
                colorIndicator = Color(0xFFFFEB3B),
                startTime = "9:30am",
                endTime = "10:00am",
                date = today
            ),
            AgendaItem(
                id = "4",
                title = "Lenin Herrera",
                duration = "30 MIN",
                time = "9:30am - 10:00am",
                colorIndicator = Color(0xFFFFEB3B),
                startTime = "9:30am",
                endTime = "10:00am",
                date = today
            ),
            AgendaItem(
                id = "4",
                title = "Lenin Herrera",
                duration = "30 MIN",
                time = "9:30am - 10:00am",
                colorIndicator = Color(0xFFFFEB3B),
                startTime = "9:30am",
                endTime = "10:00am",
                date = today
            ),
            AgendaItem(
                id = "4",
                title = "Lenin Herrera",
                duration = "30 MIN",
                time = "9:30am - 10:00am",
                colorIndicator = Color(0xFFFFEB3B),
                startTime = "9:30am",
                endTime = "10:00am",
                date = today
            ),
            AgendaItem(
                id = "4",
                title = "Lenin Herrera",
                duration = "30 MIN",
                time = "9:30am - 10:00am",
                colorIndicator = Color(0xFFFFEB3B),
                startTime = "9:30am",
                endTime = "10:00am",
                date = today
            ),
            AgendaItem(
                id = "4",
                title = "Lenin Herrera",
                duration = "30 MIN",
                time = "9:30am - 10:00am",
                colorIndicator = Color(0xFFFFEB3B),
                startTime = "9:30am",
                endTime = "10:00am",
                date = today
            )
        )
    }
    
    val agendaItems = remember(selectedDate) {
        allAgendaItems.filter { it.date == selectedDate }
    }
    
    // Estado del scroll para detectar dirección y calcular visibilidad del header
    val listState = rememberLazyListState()
    val isEmpty = agendaItems.isEmpty()
    
    // Resetear scroll cuando cambia la fecha seleccionada para mostrar el header
    LaunchedEffect(selectedDate) {
        if (listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0) {
            listState.animateScrollToItem(0)
        }
    }
    
    // Calcular alpha del header basado en el scroll
    val headerAlpha = rememberHeaderAlpha(
        listState = listState,
        isEmpty = isEmpty,
        threshold = 100
    )
    
    // Animar la altura del contenedor del header para que se colapse completamente
    // Altura aproximada: ~60dp para el header + 8dp de spacer = 68dp cuando visible
    val headerContainerHeight by animateDpAsState(
        targetValue = if (headerAlpha > 0.01f) {
            68.dp * headerAlpha
        } else {
            0.dp
        },
        animationSpec = tween(durationMillis = 300),
        label = "header_container_height"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FC))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SplashBackground)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars)
                ) {
                    // Contenedor animado para el header que se colapsa completamente
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(headerContainerHeight)
                    ) {
                        if (headerAlpha > 0.01f) {
                            Column {
                                AgendaHeader(
                                    doctorId = "1805263782",
                                    scheduleTitle = "Bienvenido Joshua!",
                                    onSearchClick = { },
                                    onNotificationClick = { },
                                    alpha = headerAlpha
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                    
                    DateHeader(
                        date = selectedDate,
                        onDateClick = { showMonthYearPicker = true }
                    )
                    
                    WeekSelector(
                        selectedDate = selectedDate,
                        onDateSelected = { selectedDate = it }
                    )
                }
            }
            
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    top = 20.dp,
                    bottom = 100.dp
                )
            ) {
                if (agendaItems.isEmpty()) {
                    item {
                        Text(
                            text = "No hay citas programadas para este día",
                            fontSize = 14.sp,
                            fontFamily = InterFontFamily(),
                            color = Color(0xFF666666),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                } else {
                    items(agendaItems) { item ->
                        AgendaCard(
                            item = item,
                            onEditClick = { editedItem ->
                                // Aquí puedes manejar la edición
                                println("Editing: ${editedItem.title}")
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
        
        BottomNavBar(
            currentRoute = currentNavItem,
            onItemClick = onNavItemClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )
    }
    
    if (showMonthYearPicker) {
        MonthYearPicker(
            currentDate = selectedDate,
            onDateSelected = { newDate ->
                selectedDate = newDate
                showMonthYearPicker = false
            },
            onDismiss = { showMonthYearPicker = false }
        )
    }
}



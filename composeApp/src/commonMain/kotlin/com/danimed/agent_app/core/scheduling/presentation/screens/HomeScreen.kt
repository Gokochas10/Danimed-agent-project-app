package com.danimed.agent_app.core.scheduling.presentation.screens

import agent_app.composeapp.generated.resources.Res
import agent_app.composeapp.generated.resources.booking
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.snapshotFlow
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import org.jetbrains.compose.resources.painterResource as resPainterResource
import com.danimed.agent_app.core.searching.presentation.screens.SearchScreen
import com.danimed.agent_app.core.notifications.presentation.screens.NotificationsScreen
import com.danimed.agent_app.core.bookings.application.viewModel.BookingsViewModel
import com.danimed.agent_app.core.bookings.domain.model.Booking
import com.danimed.agent_app.core.scheduling.presentation.components.AgendaCard
import com.danimed.agent_app.core.scheduling.presentation.components.AgendaHeader
import com.danimed.agent_app.core.scheduling.presentation.components.DateHeader
import com.danimed.agent_app.core.scheduling.presentation.components.MonthYearPicker
import com.danimed.agent_app.core.scheduling.presentation.components.WeekSelector
import com.danimed.agent_app.core.scheduling.presentation.utils.rememberHeaderAlpha
import com.danimed.agent_app.shared.components.AgendaPlaceholder
import com.danimed.agent_app.shared.components.AgendaCardPlaceholder
import com.danimed.agent_app.shared.components.BottomNavBar
import com.danimed.agent_app.shared.components.BottomNavItem
import com.danimed.agent_app.shared.components.DateHeaderPlaceholder
import com.danimed.agent_app.shared.components.WeekSelectorPlaceholder
import com.danimed.agent_app.shared.di.NotificationsModule
import com.danimed.agent_app.shared.di.BookingsModule
import com.danimed.agent_app.shared.di.NotificationsModule.getUnreadCountUseCase
import com.danimed.agent_app.shared.theme.InterFontFamily
import com.danimed.agent_app.shared.theme.PrimaryBlue
import com.danimed.agent_app.shared.theme.SplashBackground
import com.danimed.agent_app.shared.theme.White
import com.danimed.agent_app.shared.utils.SetStatusBarColor
import com.danimed.agent_app.shared.utils.currentLocalDate
import kotlinx.datetime.LocalDate

data class AgendaItem(
    val id: String,
    val title: String,
    val duration: String,
    val time: String,
    val colorIndicator: Color,
    val startTime: String,
    val endTime: String,
    val date: LocalDate,
    val status: String? = null
)

@Composable
fun HomeScreen(
    currentNavItem: BottomNavItem = BottomNavItem.Agenda,
    onNavItemClick: (BottomNavItem) -> Unit = {},
    onLogout: () -> Unit = {},
    initialSearchQuery: String? = null
) {
    when (currentNavItem) {
        BottomNavItem.Agenda -> {
            AgendaScreen(
                currentNavItem = currentNavItem,
                onNavItemClick = onNavItemClick,
                initialSearchQuery = initialSearchQuery
            )
        }
        BottomNavItem.Calendar -> {
            ScheduleScreen(
                currentNavItem = currentNavItem,
                onNavItemClick = onNavItemClick
            )
        }
        BottomNavItem.Schedule -> {
            com.danimed.agent_app.core.profile.presentation.screens.ProfileScreen(
                currentNavItem = currentNavItem,
                onNavItemClick = onNavItemClick,
                onLogout = onLogout
            )
        }
    }
}

@Composable
private fun AgendaScreen(
    currentNavItem: BottomNavItem = BottomNavItem.Agenda,
    onNavItemClick: (BottomNavItem) -> Unit = {},
    initialSearchQuery: String? = null
) {
    SetStatusBarColor(PrimaryBlue)
    
    val viewModel = remember { BookingsViewModel(BookingsModule.getBookingsUseCase) }
    val uiState by viewModel.uiState
    
    // Crear coroutine scope
    val scope = rememberCoroutineScope()
    
    // Obtener contador de notificaciones no leídas
    var unreadCount by remember { mutableStateOf(0) }
    
    // Función helper para actualizar el contador
    val updateUnreadCount: () -> Unit = {
        scope.launch {
            NotificationsModule.getUnreadCountUseCase()
                .onSuccess { count -> unreadCount = count }
                .onFailure { }
        }
    }
    
    // No inicializar con fecha local, esperar server_date
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var showMonthYearPicker by remember { mutableStateOf(false) }
    var isInitialLoad by remember { mutableStateOf(true) }
    var previousSelectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var showSearchScreen by remember { mutableStateOf(false) }
    var showNotificationsScreen by remember { mutableStateOf(false) }
    
    // Inicializar contador al cargar y cuando la app vuelve a foreground
    LaunchedEffect(Unit) {
        updateUnreadCount()
    }
    
    // Actualizar contador periódicamente y cuando la app vuelve a foreground
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(3000) // Actualizar cada 3 segundos (más frecuente)
            updateUnreadCount()
        }
    }
    var searchClickPosition by remember { mutableStateOf<Offset?>(null) }
    var currentSearchQuery by remember { mutableStateOf<String?>(initialSearchQuery) }
    
    // Cargar bookings cuando se cambia a la pestaña Agenda o cuando hay una búsqueda inicial
    LaunchedEffect(currentNavItem, initialSearchQuery) {
        if (currentNavItem == BottomNavItem.Agenda && isInitialLoad) {
            currentSearchQuery = initialSearchQuery
            viewModel.loadBookings(doctorId = 1, date = null, search = initialSearchQuery, append = false)
        }
    }
    
    // Actualizar selectedDate con server_date cuando se carga por primera vez
    LaunchedEffect(uiState.serverDate) {
        uiState.serverDate?.let { serverDateString ->
            if (selectedDate == null) {
                val serverDate = parseDateString(serverDateString)
                if (serverDate != null) {
                    selectedDate = serverDate
                    previousSelectedDate = serverDate
                    isInitialLoad = false
                }
            }
        }
    }
    
    // Cargar bookings cuando cambia la fecha seleccionada (solo si fue cambio manual, no inicialización)
    // IMPORTANTE: No hacer fetch cuando selectedDate se establece por primera vez desde server_date
    LaunchedEffect(selectedDate, currentSearchQuery) {
        selectedDate?.let { date ->
            // Solo hacer fetch si:
            // 1. Ya terminó la carga inicial (isInitialLoad = false)
            // 2. La fecha realmente cambió manualmente (no es la primera vez que se establece)
            // 3. previousSelectedDate ya estaba establecido (para evitar la primera carga duplicada)
            if (!isInitialLoad && previousSelectedDate != null && date != previousSelectedDate) {
                val dateString = formatDateForApi(date)
                viewModel.loadBookings(doctorId = 1, date = dateString, search = currentSearchQuery, append = false)
                previousSelectedDate = date
            } else if (previousSelectedDate == null && !isInitialLoad) {
                // Si es la primera vez que se establece selectedDate después de la carga inicial,
                // solo actualizar previousSelectedDate sin hacer fetch
                previousSelectedDate = date
            }
        }
    }
    
    // Convertir bookings a AgendaItems usando allBookings acumulados
    val allAgendaItems = remember(uiState.allBookings) {
        uiState.allBookings.map { booking ->
            bookingToAgendaItem(booking)
        }
    }
    
    val agendaItems = remember(selectedDate, allAgendaItems) {
        selectedDate?.let { date ->
            allAgendaItems.filter { it.date == date }
        } ?: emptyList()
    }
    
    // Estado del scroll para detectar dirección y calcular visibilidad del header
    val listState = rememberLazyListState()
    val isEmpty = agendaItems.isEmpty()
    
    // Detectar cuando se llega al final de la lista para cargar más páginas
    // Solo cuando el usuario hace scroll hacia abajo y está cerca del final
    LaunchedEffect(listState, uiState.hasMorePages, uiState.isLoadingMore) {
        var lastFirstVisibleIndex = -1
        snapshotFlow { 
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            val firstVisibleItem = layoutInfo.visibleItemsInfo.firstOrNull()?.index ?: -1
            Triple(totalItems, lastVisibleItem, firstVisibleItem)
        }.collect { (totalItems, lastVisibleItem, firstVisibleItem) ->
            // Solo cargar más si:
            // 1. Hay items en la lista
            // 2. El usuario está scrolleando hacia abajo (firstVisibleItem > lastFirstVisibleIndex)
            // 3. Estamos cerca del final (últimos 3 items)
            // 4. Hay más páginas disponibles
            // 5. No se está cargando actualmente
            val isScrollingDown = firstVisibleItem > lastFirstVisibleIndex || lastFirstVisibleIndex == -1
            
            if (totalItems > 0 && 
                isScrollingDown &&
                lastVisibleItem >= totalItems - 3 && 
                uiState.hasMorePages && 
                !uiState.isLoadingMore) {
                val dateString = selectedDate?.let { formatDateForApi(it) }
                viewModel.loadMoreBookings(
                    doctorId = 1,
                    date = dateString,
                    search = currentSearchQuery
                )
            }
            
            if (firstVisibleItem >= 0) {
                lastFirstVisibleIndex = firstVisibleItem
            }
        }
    }
    
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
                                    searchQuery = currentSearchQuery,
                                    onSearchClick = { position ->
                                        searchClickPosition = position
                                        showSearchScreen = true
                                    },
                                    onNotificationClick = { 
                                        showNotificationsScreen = true
                                    },
                                    unreadCount = unreadCount,
                                    onClearSearch = {
                                        currentSearchQuery = null
                                        selectedDate?.let { date ->
                                            val dateString = formatDateForApi(date)
                                            viewModel.loadBookings(doctorId = 1, date = dateString, search = null, append = false)
                                        } ?: run {
                                            viewModel.loadBookings(doctorId = 1, date = null, search = null, append = false)
                                        }
                                    },
                                    alpha = headerAlpha
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                    
                    // Mostrar placeholders si aún no tenemos server_date, sino mostrar componentes reales
                    val currentDate = selectedDate
                    if (currentDate == null || uiState.isLoading) {
                        DateHeaderPlaceholder()
                        WeekSelectorPlaceholder()
                    } else {
                        DateHeader(
                            date = currentDate,
                            onDateClick = { showMonthYearPicker = true }
                        )
                        
                        WeekSelector(
                            selectedDate = currentDate,
                            onDateSelected = { selectedDate = it }
                        )
                    }
                }
            }
            
            if (uiState.isLoading) {
                AgendaPlaceholder(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            } else {
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
                                serverDate = uiState.serverDate?.let { parseDateString(it) },
                                onEditClick = { editedItem ->
                                    // Aquí puedes manejar la edición
                                    println("Editing: ${editedItem.title}")
                                }
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                        
                        // Mostrar skeleton al final si se están cargando más páginas
                        if (uiState.isLoadingMore) {
                            items(2) {
                                AgendaCardPlaceholder()
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
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
        
        // Floating Action Button - Fixed position above BottomNavBar
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 90.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FloatingActionButton(
                onClick = { /* TODO: Implement create booking */ },
                modifier = Modifier.size(60.dp),
                containerColor = Color(0xFF4CAF50),
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 20.dp
                ),
                shape = CircleShape
            ) {
                Image(
                    painter = resPainterResource(Res.drawable.booking),
                    contentDescription = "Crear cita",
                    modifier = Modifier.size(30.dp),
                    colorFilter = ColorFilter.tint(White)
                )
            }
        }
    }
    
    selectedDate?.let { date ->
        if (showMonthYearPicker) {
            MonthYearPicker(
                currentDate = date,
                onDateSelected = { newDate ->
                    selectedDate = newDate
                    showMonthYearPicker = false
                },
                onDismiss = { showMonthYearPicker = false }
            )
        }
    }
    
    // Search Screen
    if (showSearchScreen) {
        SearchScreen(
            onDismiss = {
                showSearchScreen = false
                searchClickPosition = null
            },
            onSearch = { query ->
                currentSearchQuery = query
                selectedDate?.let { date ->
                    val dateString = formatDateForApi(date)
                    viewModel.loadBookings(doctorId = 1, date = dateString, search = query, append = false)
                } ?: run {
                    viewModel.loadBookings(doctorId = 1, date = null, search = query, append = false)
                }
            },
            initialClickPosition = searchClickPosition,
            modifier = Modifier.fillMaxSize())
    }
    
    // Notifications Screen
    if (showNotificationsScreen) {
        NotificationsScreen(
            onBack = {
                showNotificationsScreen = false
                // Actualizar contador después de salir de la pantalla
                updateUnreadCount()
            }
        )
    }
}

/**
 * Convierte un Booking del dominio a un AgendaItem para la UI
 */
private fun bookingToAgendaItem(booking: Booking): AgendaItem {
    val date = parseDateString(booking.booking_date) ?: currentLocalDate()
    val startTimeFormatted = formatTime(booking.start_time)
    val endTimeFormatted = formatTime(booking.end_time)
    val timeRange = "$startTimeFormatted - $endTimeFormatted"
    val duration = "${booking.duration_minutes} MIN"
    
    // Color basado en la duración o estado (puedes ajustar la lógica)
    val colorIndicator = when {
        booking.duration_minutes >= 60 -> Color(0xFF4FC3F7)
        booking.duration_minutes >= 30 -> Color(0xFFFFEB3B)
        else -> Color(0xFF4CAF50)
    }
    
    return AgendaItem(
        id = booking.booking_id.toString(),
        title = booking.patient_name,
        duration = duration,
        time = timeRange,
        colorIndicator = colorIndicator,
        startTime = startTimeFormatted,
        endTime = endTimeFormatted,
        date = date,
        status = booking.status
    )
}

/**
 * Parsea una fecha en formato "2026-01-07" a LocalDate
 */
private fun parseDateString(dateString: String): LocalDate? {
    return try {
        val parts = dateString.split("-")
        if (parts.size == 3) {
            LocalDate(
                year = parts[0].toInt(),
                month = kotlinx.datetime.Month(parts[1].toInt()),
                dayOfMonth = parts[2].toInt()
            )
        } else {
            null
        }
    } catch (e: Exception) {
        null
    }
}

/**
 * Formatea una hora en formato "10:00:00" a "10:00am"
 */
private fun formatTime(timeString: String): String {
    return try {
        val parts = timeString.split(":")
        if (parts.size >= 2) {
            val hour = parts[0].toInt()
            val minute = parts[1].toInt()
            val period = if (hour < 12) "am" else "pm"
            val displayHour = when {
                hour == 0 -> 12
                hour > 12 -> hour - 12
                else -> hour
            }
            "$displayHour:${minute.toString().padStart(2, '0')}$period"
        } else {
            timeString
        }
    } catch (e: Exception) {
        timeString
    }
}/**
 * Formatea una LocalDate a formato "2026-01-07" para la API
 */
private fun formatDateForApi(date: LocalDate): String {
    val month = date.monthNumber.toString().padStart(2, '0')
    val day = date.dayOfMonth.toString().padStart(2, '0')
    return "${date.year}-$month-$day"
}
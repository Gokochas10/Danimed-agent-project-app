package com.danimed.agent_app.core.scheduling.presentation.screens

import agent_app.composeapp.generated.resources.Res
import agent_app.composeapp.generated.resources.calendar_add
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.flow.distinctUntilChanged
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import com.danimed.agent_app.core.scheduling.presentation.application.viewModel.ScheduleViewModel
import com.danimed.agent_app.core.scheduling.presentation.domain.model.Schedule
import com.danimed.agent_app.core.scheduling.presentation.utils.DateFormatter
import com.danimed.agent_app.shared.components.BottomNavBar
import com.danimed.agent_app.shared.components.BottomNavItem
import com.danimed.agent_app.shared.components.SchedulePlaceholder
import com.danimed.agent_app.shared.di.ScheduleModule
import com.danimed.agent_app.shared.theme.InterFontFamily
import com.danimed.agent_app.shared.theme.PrimaryBlue
import com.danimed.agent_app.shared.theme.SplashBackground
import com.danimed.agent_app.shared.theme.White
import com.danimed.agent_app.shared.utils.SetStatusBarColor
import compose.icons.FeatherIcons
import compose.icons.feathericons.Info
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import org.jetbrains.compose.resources.painterResource as resPainterResource

data class ScheduleBlock(
    val schedule: Schedule,
    val startHour: Int,
    val startMinute: Int,
    val endHour: Int,
    val endMinute: Int,
    val dayOfWeek: DayOfWeek
)

@Composable
fun ScheduleScreen(
    currentNavItem: BottomNavItem = BottomNavItem.Calendar,
    onNavItemClick: (BottomNavItem) -> Unit = {},
    doctorId: Int = 1
) {
    SetStatusBarColor(PrimaryBlue)

    val viewModel = remember {
        ScheduleViewModel(
            ScheduleModule.getSchedulesUseCase
        )
    }
    var selectedSchedule by remember { mutableStateOf<Schedule?>(null) }
    var showInfoDialog by remember { mutableStateOf(false) }

    LaunchedEffect(doctorId) {
        viewModel.loadSchedules(doctorId)
    }

    val uiState by viewModel.uiState
    
    // Scroll state para detectar scroll y animar header
    val scrollState = rememberScrollState()
    
    // Calcular alpha del header basado en el scroll
    val headerAlpha = rememberScrollHeaderAlpha(
        scrollState = scrollState,
        threshold = 100
    )
    
    // Animar la altura del contenedor del header
    val headerContainerHeight by animateDpAsState(
        targetValue = if (headerAlpha > 0.01f) {
            (80.dp * headerAlpha).coerceAtLeast(0.dp)
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
            modifier = Modifier.fillMaxSize()
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
                            ScheduleHeader(
                                onInfoClick = { showInfoDialog = true },
                                alpha = headerAlpha
                            )
                        }
                    }
                }
            }

            // Contenido: mostrar placeholder mientras carga, o contenido real cuando termine
            if (uiState.isLoading) {
                // Placeholder que cubre TODA el área (incluyendo WeekHeader y TimetableGrid)
                SchedulePlaceholder(
                    modifier = Modifier.weight(1f)
                )
            } else {
                // Week header (Mon-Sun) - solo visible cuando no está cargando
                WeekHeader()

                // Timetable grid con contenido real
                TimetableGrid(
                    schedules = uiState.schedules,
                    isLoading = uiState.isLoading,
                    scrollState = scrollState,
                    onScheduleClick = { schedule ->
                        selectedSchedule = schedule
                    },
                    modifier = Modifier.weight(1f)
                )
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
                onClick = { /* TODO: Implement create schedule */ },
                modifier = Modifier.size(60.dp),
                containerColor = Color(0xFF4CAF50),
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 20.dp
                ),
                shape = CircleShape
            ) {
                Image(
                    painter = resPainterResource(Res.drawable.calendar_add),
                    contentDescription = "Crear horario",
                    modifier = Modifier.size(30.dp),
                    colorFilter = ColorFilter.tint(White)
                )
            }
        }
    }

    // Dialog para mostrar detalles del horario
    selectedSchedule?.let { schedule ->
        ScheduleDetailDialog(
            schedule = schedule,
            onDismiss = { selectedSchedule = null }
        )
    }
    
    // Dialog informativo sobre colores
    if (showInfoDialog) {
        InfoDialog(
            onDismiss = { showInfoDialog = false }
        )
    }
}

@Composable
private fun ScheduleDetailDialog(
    schedule: Schedule,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Título
                Text(
                    text = "Detalles del Horario",
                    fontSize = 20.sp,
                    fontFamily = InterFontFamily(),
                    fontWeight = FontWeight.Bold,
                    color = SplashBackground
                )

                // Fecha
                DetailRow(
                    label = "Fecha",
                    value = formatDate(schedule.date)
                )

                // Hora de inicio
                DetailRow(
                    label = "Hora de Inicio",
                    value = schedule.start_time
                )

                // Hora de finalización
                DetailRow(
                    label = "Hora de Finalización",
                    value = schedule.end_time
                )

                // Estado
                DetailRow(
                    label = "Estado",
                    value = if (schedule.is_active) "Activo" else "Inactivo"
                )

                // Indicador visual de estado
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                if (schedule.is_active) PrimaryBlue
                                else Color(0xFFCCCCCC)
                            )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (schedule.is_active) "Horario Activo" else "Horario Inactivo",
                        fontSize = 14.sp,
                        fontFamily = InterFontFamily(),
                        color = if (schedule.is_active) PrimaryBlue else Color(0xFF666666),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontFamily = InterFontFamily(),
            color = Color(0xFF666666),
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontFamily = InterFontFamily(),
            color = Color(0xFF333333),
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
private fun InfoDialog(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Título
                Text(
                    text = "Información de Colores",
                    fontSize = 20.sp,
                    fontFamily = InterFontFamily(),
                    fontWeight = FontWeight.Bold,
                    color = SplashBackground
                )
                
                // Color verde - Disponible
                ColorInfoRow(
                    color = Color(0xFF4CAF50),
                    label = "Disponible",
                    description = "Horarios disponibles para agendar"
                )
                
                // Color azul - Agendado
                ColorInfoRow(
                    color = PrimaryBlue,
                    label = "Agendado",
                    description = "Horarios que ya tienen citas asignadas"
                )
                
                // Color rojo - Cancelado
                ColorInfoRow(
                    color = Color(0xFFF44336),
                    label = "Cancelado",
                    description = "Horarios cancelados o no disponibles"
                )
            }
        }
    }
}

@Composable
private fun ColorInfoRow(
    color: Color,
    label: String,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(color)
        )
        
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = label,
                fontSize = 16.sp,
                fontFamily = InterFontFamily(),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
            Text(
                text = description,
                fontSize = 14.sp,
                fontFamily = InterFontFamily(),
                color = Color(0xFF666666)
            )
        }
    }
}

@Composable
private fun ScheduleHeader(
    onInfoClick: () -> Unit,
    alpha: Float
) {
    // Animar el alpha suavemente
    val animatedAlpha by animateFloatAsState(
        targetValue = alpha,
        animationSpec = tween(durationMillis = 500),
        label = "header_alpha"
    )
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp)
            .alpha(animatedAlpha)
            .graphicsLayer {
                // También animar la posición vertical para un efecto más suave
                translationY = (1f - animatedAlpha) * -20f
            }
    ) {
        // Centered title
        Text(
            text = "Mi Horario",
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            fontFamily = InterFontFamily(),
            fontWeight = FontWeight.Bold,
            color = White,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        )
        
        // Info icon in bottom right
        IconButton(
            onClick = onInfoClick,
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Icon(
                painter = rememberVectorPainter(image = FeatherIcons.Info),
                contentDescription = "Información",
                tint = White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun WeekHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFEFF4FF))
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val daysOfWeek = listOf(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY,
            DayOfWeek.SUNDAY
        )

        // Empty cell for time column
        Box(
            modifier = Modifier.width(60.dp)
        )

        daysOfWeek.forEach { dayOfWeek ->
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = DateFormatter.formatDayOfWeek(dayOfWeek).take(3),
                    fontSize = 12.sp,
                    fontFamily = InterFontFamily(),
                    fontWeight = FontWeight.Bold,
                    color = SplashBackground
                )
            }
        }
    }
}

@Composable
private fun TimetableGrid(
    schedules: List<Schedule>,
    isLoading: Boolean,
    scrollState: ScrollState,
    onScheduleClick: (Schedule) -> Unit,
    modifier: Modifier = Modifier
) {
    val hours = (0..23).toList()

    // Parse schedules into blocks
    val scheduleBlocks = remember(schedules) {
        schedules.mapNotNull { schedule ->
            val date = parseDate(schedule.date)
            val (startHour, startMinute) = parseTime(schedule.start_time)
            val (endHour, endMinute) = parseTime(schedule.end_time)

            ScheduleBlock(
                schedule = schedule,
                startHour = startHour,
                startMinute = startMinute,
                endHour = endHour,
                endMinute = endMinute,
                dayOfWeek = date.dayOfWeek
            )
        }
    }

    BoxWithConstraints(modifier = modifier) {
        val timeColumnWidth = 60.dp
        val dayColumnWidth = (maxWidth - timeColumnWidth) / 7f

        Box(modifier = Modifier.fillMaxSize()) {
            // Grid background with blocks (scrollable)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((hours.size * 60).dp) // Altura total fija
                ) {
                    // Grid rows
                    Column {
                        hours.forEach { hour ->
                            TimetableRow(
                                hour = hour,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    // Schedule blocks overlay (positioned relative to grid)
                    ScheduleBlocksOverlay(
                        blocks = scheduleBlocks,
                        timeColumnWidth = timeColumnWidth,
                        dayColumnWidth = dayColumnWidth,
                        onScheduleClick = onScheduleClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun TimetableRow(
    hour: Int,
    modifier: Modifier = Modifier
) {
    val daysOfWeek = listOf(
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY,
        DayOfWeek.SUNDAY
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Time label
        Box(
            modifier = Modifier
                .width(60.dp)
                .fillMaxHeight()
                .background(Color(0xFFF7F9FC)),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = "${hour.toString().padStart(2, '0')}:00",
                fontSize = 11.sp,
                fontFamily = InterFontFamily(),
                color = Color(0xFF666666),
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        // Day cells
        daysOfWeek.forEach { _ ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .border(0.5.dp, Color(0xFFE0E0E0))
                    .background(Color.White)
            )
        }
    }
}

@Composable
private fun ScheduleBlocksOverlay(
    blocks: List<ScheduleBlock>,
    timeColumnWidth: Dp,
    dayColumnWidth: Dp,
    onScheduleClick: (Schedule) -> Unit,
    modifier: Modifier = Modifier
) {
    val daysOfWeek = listOf(
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY,
        DayOfWeek.SUNDAY
    )
    val hourHeight = 60.dp

    Box(modifier = modifier) {
        blocks.forEach { block ->
            val dayIndex = daysOfWeek.indexOf(block.dayOfWeek)
            if (dayIndex >= 0) {
                val startMinutes = block.startHour * 60 + block.startMinute
                val endMinutes = block.endHour * 60 + block.endMinute
                val durationMinutes = endMinutes - startMinutes

                // Cálculo exacto del offset superior
                val topOffset = (startMinutes.toFloat() / 60f) * hourHeight
                val height = (durationMinutes.toFloat() / 60f) * hourHeight
                val leftOffset = timeColumnWidth + (dayIndex * dayColumnWidth)

                Box(
                    modifier = Modifier
                        .offset(x = leftOffset, y = topOffset)
                        .width(dayColumnWidth)
                        .height(height.coerceAtLeast(20.dp))
                        .padding(horizontal = 2.dp, vertical = 1.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(getScheduleColor(block.schedule))
                        .clickable { onScheduleClick(block.schedule) }
                        .zIndex(1f),
                    contentAlignment = Alignment.Center
                ) {
                    // Opcional: mostrar tiempo en el bloque si es suficientemente grande
//                    if (durationMinutes >= 30) {
//                        Text(
//                            text = "${block.schedule.start_time.take(5)}",
//                            fontSize = 10.sp,
//                            fontFamily = InterFontFamily(),
//                            color = White,
//                            fontWeight = FontWeight.Medium
//                        )
//                    }
                }
            }
        }
    }
}

private fun parseDate(dateString: String): LocalDate {
    val parts = dateString.split("-")
    return LocalDate(
        year = parts[0].toInt(),
        month = Month(parts[1].toInt()),
        dayOfMonth = parts[2].toInt()
    )
}

private fun parseTime(timeString: String): Pair<Int, Int> {
    val parts = timeString.split(":")
    return parts[0].toInt() to parts[1].toInt()
}

private fun getScheduleColor(schedule: Schedule): Color {
    return when {
        schedule.is_active -> PrimaryBlue.copy(alpha = 0.8f)
        else -> Color(0xFFCCCCCC).copy(alpha = 0.5f)
    }
}

private fun formatDate(dateString: String): String {
    val date = parseDate(dateString)
    val dayName = DateFormatter.formatDayOfWeek(date.dayOfWeek)
    return "$dayName, ${date.dayOfMonth}/${date.monthNumber}/${date.year}"
}

/**
 * Hook para calcular el alpha del header basado en el scroll de un ScrollState.
 * Similar a rememberHeaderAlpha pero para ScrollState en lugar de LazyListState.
 */
@Composable
private fun rememberScrollHeaderAlpha(
    scrollState: ScrollState,
    threshold: Int = 100
): Float {
    var alpha by remember { mutableStateOf(1f) }
    var previousValue by remember { mutableStateOf(0) }
    var scrollDirection by remember { mutableStateOf<ScrollDirection?>(null) }
    
    // Observar cambios en el scroll usando snapshotFlow
    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.value }
            .distinctUntilChanged()
            .collect { currentValue ->
                // Si no hay scroll, siempre mostrar el header
                if (currentValue == 0) {
                    alpha = 1f
                    previousValue = currentValue
                    scrollDirection = null
                    return@collect
                }
                
                // Detectar dirección del scroll
                val newDirection = when {
                    currentValue < previousValue -> ScrollDirection.UP
                    currentValue > previousValue -> ScrollDirection.DOWN
                    else -> scrollDirection // Mantener dirección anterior
                }
                
                scrollDirection = newDirection
                
                // Calcular alpha basado en la dirección y cantidad de scroll
                alpha = when {
                    // Al inicio (top), siempre visible
                    currentValue == 0 -> 1f
                    // Scrolling down - desaparecer progresivamente
                    newDirection == ScrollDirection.DOWN -> {
                        val progress = (currentValue.coerceIn(0, threshold).toFloat() / threshold)
                        (1f - progress).coerceIn(0f, 1f)
                    }
                    // Scrolling up - aparecer progresivamente
                    newDirection == ScrollDirection.UP -> {
                        val progress = (currentValue.coerceIn(0, threshold).toFloat() / threshold)
                        (1f - progress).coerceIn(0f, 1f)
                    }
                    // Sin movimiento, mantener alpha actual
                    else -> alpha
                }
                
                previousValue = currentValue
            }
    }
    
    return alpha
}

/**
 * Enum para representar la dirección del scroll
 */
private enum class ScrollDirection {
    UP, DOWN
}
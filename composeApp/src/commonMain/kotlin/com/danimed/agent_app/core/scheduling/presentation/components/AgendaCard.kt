package com.danimed.agent_app.core.scheduling.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutBounce
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.danimed.agent_app.core.bookings.application.viewModel.BookingEventsViewModel
import com.danimed.agent_app.core.scheduling.presentation.screens.AgendaItem
import com.danimed.agent_app.shared.components.BookingEventCard
import com.danimed.agent_app.shared.components.BookingEventsPlaceholder
import com.danimed.agent_app.shared.di.BookingsModule
import com.danimed.agent_app.shared.theme.InterFontFamily
import com.danimed.agent_app.shared.theme.SplashBackground
import compose.icons.FeatherIcons
import compose.icons.feathericons.*
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import com.danimed.agent_app.shared.theme.White
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

@Composable
fun AgendaCard(
    item: AgendaItem,
    serverDate: kotlinx.datetime.LocalDate? = null,
    onEditClick: (AgendaItem) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var cardWidth by remember { mutableFloatStateOf(0f) }

    // Usar Animatable para control total de la animación
    val offsetXAnim = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    // Umbral para mostrar el diálogo (50% del ancho)
    val threshold = cardWidth * 0.5f

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)) // Clip primero
            .pointerInput(item.id) {
                detectHorizontalDragGestures(
                    onDragStart = {
                        // Cancelar cualquier animación en curso
                        scope.launch {
                            offsetXAnim.stop()
                        }
                    },
                    onDragEnd = {
                        scope.launch {
                            val currentOffset = offsetXAnim.value
                            if (currentOffset <= -threshold) {
                                // Supera el umbral - animar a 0 y mostrar diálogo
                                offsetXAnim.animateTo(
                                    targetValue = 0f,
                                    animationSpec = tween(durationMillis = 200)
                                )
                                showEditDialog = true
                            } else {
                                // No supera - animar de regreso a 0
                                offsetXAnim.animateTo(
                                    targetValue = 0f,
                                    animationSpec = tween(durationMillis = 200)
                                )
                            }
                        }
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        scope.launch {
                            val newOffset = (offsetXAnim.value + dragAmount).coerceAtMost(0f)
                            offsetXAnim.snapTo(newOffset.coerceAtLeast(-cardWidth))
                        }
                    }
                )
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFFFF)
        )
    ) {
        // Layout personalizado con padding interno
        SwipeRevealLayout(
            offset = offsetXAnim.value,
            onWidthMeasured = { cardWidth = it },
            modifier = Modifier.padding(14.dp),
            frontContent = {
                CardContent(item = item)
            },
            backContent = {
                EditButton()
            }
        )
    }

    // Diálogo de edición
    if (showEditDialog) {
        Dialog(onDismissRequest = { showEditDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                EditForm(
                    item = item,
                    serverDate = serverDate,
                    onSave = {
                        onEditClick(item)
                        showEditDialog = false
                    },
                    onCancel = {
                        showEditDialog = false
                    }
                )
            }
        }
    }
}

@Composable
fun EditSwipeIndicator() {

    val transition = rememberInfiniteTransition()
    val bounce by transition.animateFloat(
        initialValue = 0f,
        targetValue = -8f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 800,
                easing = EaseOutBounce
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .size(40.dp) // ← Mantiene un área pequeña dentro del Row
            .offset(x = 0.dp, y = (-6).dp) // ← Sube el contenido y evita empujar el Row
    ) {

        Column(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                painter = rememberVectorPainter(image = FeatherIcons.Eye),
                contentDescription = "Ver Detalles",
                tint = SplashBackground,
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.height(1.dp))

            Text(
                text = "Desliza",
                color = SplashBackground,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.offset(y = bounce.dp)
            )
        }
    }
}


@Composable
private fun SwipeRevealLayout(
    offset: Float,
    onWidthMeasured: (Float) -> Unit,
    modifier: Modifier = Modifier,
    frontContent: @Composable () -> Unit,
    backContent: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = {
            // Card de fondo (azul de edición) - DEBE cubrir TODO
            Box(
                modifier = Modifier
                    .layoutId("back")
                    .fillMaxWidth()
            ) {
                backContent()
            }
            // Card de frente (contenido normal)
            Box(
                modifier = Modifier.layoutId("front")
            ) {
                frontContent()
            }
        }
    ) { measurables, constraints ->
        val backMeasurable = measurables.first { it.layoutId == "back" }
        val frontMeasurable = measurables.first { it.layoutId == "front" }

        // Medir el contenido frontal
        val frontPlaceable = frontMeasurable.measure(constraints)

        // El back debe ser MÁS GRANDE para cubrir todo incluyendo los bordes redondeados
        // Agregar padding extra para asegurar cobertura total
        val extraPadding = 190 // Pixels extra para cubrir completamente
        val backConstraints = Constraints.fixed(
            width = frontPlaceable.width + extraPadding,
            height = frontPlaceable.height + extraPadding
        )
        val backPlaceable = backMeasurable.measure(backConstraints)

        val width = frontPlaceable.width
        val height = frontPlaceable.height

        onWidthMeasured(width.toFloat())

        layout(width, height) {
            // Posicionar el card de fondo
            // Comenzar más allá del borde derecho para estar completamente oculto
            val backX = (width + offset).roundToInt()
            val backY = -extraPadding / 2 // Centrar verticalmente el padding extra

            // Colocar el back si está parcialmente visible
            if (backX < width + extraPadding) {
                backPlaceable.placeRelative(backX, backY)
            }

            // Posicionar el card de frente
            val frontX = offset.roundToInt()
            frontPlaceable.placeRelative(frontX, 0)
        }
    }
}

@Composable
private fun EditButton() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = SplashBackground,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        // Ahora está centrado verticalmente y alineado a la izquierda
        Row(
            modifier = Modifier
                .align(Alignment.CenterStart) // ← centro vertical + inicio horizontal
                .padding(start = 80.dp),       // pequeño padding a la izquierda
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = rememberVectorPainter(image = FeatherIcons.Edit),
                contentDescription = "Ver Detalles",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Ver Detalles",
                fontSize = 18.sp,
                fontFamily = InterFontFamily(),
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun CardContent(item: AgendaItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color(0xFFFFFFFF))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                painter = rememberVectorPainter(image = FeatherIcons.Activity),
                contentDescription = "Estado",
                tint = SplashBackground,
                modifier = Modifier.size(20.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = rememberVectorPainter(image = FeatherIcons.Clock),
                        contentDescription = "Tiempo",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = item.duration,
                    fontSize = 12.sp,
                    fontFamily = InterFontFamily(),
                    color = Color(0xFF4CAF50),
                    textAlign = TextAlign.Center
                )
            }

            EditSwipeIndicator()
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = rememberVectorPainter(image = FeatherIcons.User),
                contentDescription = "Paciente",
                tint = SplashBackground,
                modifier = Modifier.size(16.dp)
            )

            Spacer(modifier = Modifier.size(8.dp))

            Text(
                text = item.title,
                fontSize = 18.sp,
                fontFamily = InterFontFamily(),
                fontWeight = FontWeight.Bold,
                color = SplashBackground
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            ) {
                Icon(
                    painter = rememberVectorPainter(image = FeatherIcons.Calendar),
                    contentDescription = "Horario",
                    tint = SplashBackground,
                    modifier = Modifier.size(14.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = item.time,
                    fontSize = 13.sp,
                    fontFamily = InterFontFamily(),
                    color = SplashBackground
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.End)
            ) {
                if (item.status != null) {
                    Icon(
                        painter = rememberVectorPainter(image = FeatherIcons.Info),
                        contentDescription = "Estado",
                        tint = Color(0xFFF44336),
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.size(2.dp))

                    Text(
                        modifier = Modifier.padding(end = 6.dp),
                        text = item.status,
                        fontSize = 11.sp,
                        fontFamily = InterFontFamily(),
                        color = Color(0xFFF44336)
                    )
                } else {
                    Icon(
                        painter = rememberVectorPainter(image = FeatherIcons.HelpCircle),
                        contentDescription = "Estado desconocido",
                        tint = Color(0xFF666666),
                        modifier = Modifier.size(20.dp).padding(end = 6.dp),
                    )
                }
            }
        }
    }
}
@OptIn(ExperimentalTime::class)
@Composable
private fun EditForm(
    item: AgendaItem,
    serverDate: kotlinx.datetime.LocalDate? = null,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    val bookingId = item.id.toIntOrNull() ?: 0
    val eventsViewModel = remember { BookingEventsViewModel(BookingsModule.getBookingEventsUseCase) }
    val eventsUiState by eventsViewModel.uiState
    var showHistory by remember { mutableStateOf(false) }
    var formHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    LaunchedEffect(bookingId, showHistory) {
        if (bookingId > 0 && showHistory) {
            eventsViewModel.loadEvents(bookingId)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (formHeight > 0.dp) {
                    Modifier.height(formHeight + 32.dp) // Agregar padding
                } else {
                    Modifier.wrapContentHeight()
                }
            )
            .padding(16.dp)
    ) {
        // Icono X para cerrar en la esquina superior derecha (siempre visible)
        Icon(
            painter = rememberVectorPainter(image = FeatherIcons.X),
            contentDescription = "Cerrar",
            tint = Color(0xFFF44336),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(24.dp)
                .clickable { onCancel() }
                .zIndex(10f)
        )

        // Flecha hacia la izquierda para volver (solo visible cuando se muestra el historial)
        if (showHistory) {
            Icon(
                painter = rememberVectorPainter(image = FeatherIcons.ArrowLeft),
                contentDescription = "Volver",
                tint = SplashBackground,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .size(24.dp)
                    .clickable { showHistory = false }
                    .zIndex(10f)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            // Formulario (se desliza hacia arriba y desaparece)
            AnimatedVisibility(
                visible = !showHistory,
                exit = slideOutVertically(
                    targetOffsetY = { -it },
                    animationSpec = tween(durationMillis = 400)
                ) + fadeOut(
                    animationSpec = tween(durationMillis = 400)
                ),
                enter = slideInVertically(
                    initialOffsetY = { 0 },
                    animationSpec = tween(durationMillis = 400)
                ) + fadeIn(
                    animationSpec = tween(durationMillis = 400)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .onGloballyPositioned { coordinates ->
                            if (!showHistory && formHeight == 0.dp) {
                                formHeight = with(density) { coordinates.size.height.toDp() }
                            }
                        }
                        .verticalScroll(rememberScrollState())
                ) {
                    // Título centrado
                    Text(
                        text = "Ver Detalles Cita",
                        fontSize = 20.sp,
                        fontFamily = InterFontFamily(),
                        fontWeight = FontWeight.Bold,
                        color = SplashBackground,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 16.dp),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Campo de título
                    Text(
                        text = "Paciente",
                        fontSize = 14.sp,
                        fontFamily = InterFontFamily(),
                        color = SplashBackground,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = item.title,
                        fontSize = 12.sp,
                        fontFamily = InterFontFamily(),
                        color = SplashBackground,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    )

                    // Campo de tiempo y duración en la misma fila
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.weight(0.60f)
                        ) {
                            Text(
                                text = "Horario",
                                fontSize = 14.sp,
                                fontFamily = InterFontFamily(),
                                color = SplashBackground,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Text(
                                text = item.time,
                                fontSize = 12.sp,
                                fontFamily = InterFontFamily(),
                                color = SplashBackground,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                                    .padding(10.dp)
                            )
                        }

                        Column(
                            modifier = Modifier.weight(0.4f)
                        ) {
                            Text(
                                text = "Duración",
                                fontSize = 14.sp,
                                fontFamily = InterFontFamily(),
                                color = SplashBackground,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Text(
                                text = item.duration,
                                fontSize = 12.sp,
                                fontFamily = InterFontFamily(),
                                color = SplashBackground,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                                    .padding(10.dp)
                            )
                        }
                    }

                    // Campo de estado
                    Text(
                        text = "Estado",
                        fontSize = 14.sp,
                        fontFamily = InterFontFamily(),
                        color = SplashBackground,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (item.status != null) {
                            Icon(
                                painter = rememberVectorPainter(image = FeatherIcons.Info),
                                contentDescription = "Estado",
                                tint = Color(0xFFF44336),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = item.status,
                                fontSize = 12.sp,
                                fontFamily = InterFontFamily(),
                                color = SplashBackground
                            )
                        } else {
                            Icon(
                                painter = rememberVectorPainter(image = FeatherIcons.HelpCircle),
                                contentDescription = "Estado desconocido",
                                tint = Color(0xFF666666),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Sin estado",
                                fontSize = 12.sp,
                                fontFamily = InterFontFamily(),
                                color = Color(0xFF666666)
                            )
                        }
                    }

                    // Botones de acción
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Determinar si la cita es pasada comparando fecha y hora
                        val isPastAppointment = remember(item.date, item.startTime, serverDate) {
                            serverDate?.let { server ->
                                // Comparar fecha primero
                                when {
                                    item.date < server -> true // Fecha pasada
                                    item.date > server -> false // Fecha futura
                                    else -> {
                                        // Misma fecha, comparar hora
                                        // Parsear hora de inicio (formato: "10:00am")
                                        val timeParts = item.startTime.replace("am", "").replace("pm", "").split(":")
                                        if (timeParts.size >= 2) {
                                            val hour = timeParts[0].toIntOrNull() ?: 0
                                            val minute = timeParts[1].toIntOrNull() ?: 0
                                            val isPm = item.startTime.contains("pm", ignoreCase = true)
                                            val hour24 = when {
                                                isPm && hour != 12 -> hour + 12
                                                !isPm && hour == 12 -> 0
                                                else -> hour
                                            }
                                            
                                            // Obtener hora actual del sistema (asumiendo que serverDate es la fecha actual del servidor)
                                            val now = kotlinx.datetime.Clock.System.now()
                                            val timeZone = kotlinx.datetime.TimeZone.currentSystemDefault()
                                            val localDateTime = now.toLocalDateTime(timeZone)
                                            
                                            // Comparar hora
                                            val appointmentHour = hour24 * 60 + minute
                                            val currentHourMinutes = localDateTime.hour * 60 + localDateTime.minute
                                            appointmentHour < currentHourMinutes
                                        } else {
                                            false
                                        }
                                    }
                                }
                            } ?: false
                        }
                        
                        Button(
                            onClick = {
                                // TODO: Implementar lógica de reagendar
                            },
                            enabled = !isPastAppointment,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SplashBackground,
                                disabledContainerColor = Color(0xFFCCCCCC),
                                disabledContentColor = Color(0xFF999999)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Reagendar Cita",
                                color = if (isPastAppointment) Color(0xFF999999) else Color.White,
                                fontFamily = InterFontFamily(),
                                fontSize = 16.sp
                            )
                        }

                        Button(
                            onClick = {
                                showHistory = true
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4CAF50)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Ver Historial de Cita",
                                color = White,
                                fontFamily = InterFontFamily(),
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }

            // Historial de eventos (aparece desde abajo y ocupa todo el espacio)
            AnimatedVisibility(
                visible = showHistory,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(durationMillis = 400)
                ) + fadeIn(
                    animationSpec = tween(durationMillis = 400)
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(durationMillis = 400)
                ) + fadeOut(
                    animationSpec = tween(durationMillis = 400)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(
                            if (formHeight > 0.dp) {
                                Modifier.height(formHeight)
                            } else {
                                Modifier.wrapContentHeight()
                            }
                        )
                ) {
                    // Título del historial
                    Text(
                        text = "Historial de Citas",
                        fontSize = 20.sp,
                        fontFamily = InterFontFamily(),
                        fontWeight = FontWeight.Bold,
                        color = SplashBackground,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp, bottom = 24.dp),
                        textAlign = TextAlign.Center
                    )

                    // Contenido scrollable que ocupa el resto del espacio
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        if (eventsUiState.isLoading) {
                            BookingEventsPlaceholder(
                                modifier = Modifier.fillMaxSize()
                            )
                        } else if (eventsUiState.events.isEmpty()) {
                            Text(
                                text = "No hay eventos registrados",
                                fontSize = 14.sp,
                                fontFamily = InterFontFamily(),
                                color = Color(0xFF666666),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(vertical = 16.dp),
                                textAlign = TextAlign.Center
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(vertical = 8.dp)
                            ) {
                                items(eventsUiState.events) { event ->
                                    BookingEventCard(event = event)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
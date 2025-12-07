package com.danimed.agent_app.core.scheduling.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutBounce
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danimed.agent_app.core.scheduling.presentation.screens.AgendaItem
import com.danimed.agent_app.shared.theme.InterFontFamily
import com.danimed.agent_app.shared.theme.SplashBackground
import compose.icons.FeatherIcons
import compose.icons.feathericons.*
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun AgendaCard(
    item: AgendaItem,
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
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                EditForm(
                    item = item,
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
                painter = rememberVectorPainter(image = FeatherIcons.Edit),
                contentDescription = "Editar",
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
                contentDescription = "Editar",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Editar",
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
                Icon(
                    painter = rememberVectorPainter(image = FeatherIcons.Info),
                    contentDescription = "Estado",
                    tint = Color(0xFFF44336),
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.size(2.dp))

                Text(
                    text = "Cancelada",
                    fontSize = 14.sp,
                    fontFamily = InterFontFamily(),
                    color = Color(0xFFF44336)
                )
            }
        }
    }
}
@Composable
private fun EditForm(
    item: AgendaItem,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    var title by remember { mutableStateOf(item.title) }
    var time by remember { mutableStateOf(item.time) }
    var duration by remember { mutableStateOf(item.duration) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Editar Cita",
            fontSize = 20.sp,
            fontFamily = InterFontFamily(),
            fontWeight = FontWeight.Bold,
            color = SplashBackground,
            modifier = Modifier.padding(bottom = 16.dp)
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
            text = title,
            fontSize = 16.sp,
            fontFamily = InterFontFamily(),
            color = SplashBackground,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                .padding(12.dp)
        )

        // Campo de tiempo
        Text(
            text = "Horario",
            fontSize = 14.sp,
            fontFamily = InterFontFamily(),
            color = SplashBackground,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = time,
            fontSize = 16.sp,
            fontFamily = InterFontFamily(),
            color = SplashBackground,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                .padding(12.dp)
        )

        // Campo de duración
        Text(
            text = "Duración",
            fontSize = 14.sp,
            fontFamily = InterFontFamily(),
            color = SplashBackground,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = duration,
            fontSize = 16.sp,
            fontFamily = InterFontFamily(),
            color = SplashBackground,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                .padding(12.dp)
        )

        // Botones de acción
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onCancel,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE0E0E0)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Cancelar",
                    color = Color(0xFF666666),
                    fontFamily = InterFontFamily()
                )
            }

            Button(
                onClick = onSave,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SplashBackground
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Guardar",
                    color = Color.White,
                    fontFamily = InterFontFamily()
                )
            }
        }
    }
}
package com.danimed.agent_app.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danimed.agent_app.shared.theme.InterFontFamily
import com.revenuecat.placeholder.PlaceholderDefaults
import com.revenuecat.placeholder.placeholder
import kotlinx.datetime.DayOfWeek

/**
 * Componente placeholder que simula el contenido de la pantalla de horarios
 * mientras se carga la información desde el servidor.
 * Usa placeholder-compose de RevenueCat para efectos de animación.
 * Muestra las horas reales y un placeholder grande que cubre toda el área de la cuadrícula.
 */
@Composable
fun SchedulePlaceholder(
    modifier: Modifier = Modifier
) {
    val hours = (0..23).toList()
    val daysOfWeek = listOf(
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY,
        DayOfWeek.SUNDAY
    )
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FC))
    ) {
        // Week header placeholder (similar a WeekHeader real)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFEFF4FF))
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Celda vacía para la columna de tiempo
            Box(modifier = Modifier.width(60.dp))
            
            // 7 días de la semana con placeholder animado
            daysOfWeek.forEach { _ ->
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    // Placeholder para el nombre del día con animación shimmer
                    Box(
                        modifier = Modifier
                            .width(30.dp)
                            .height(12.dp)
                            .placeholder(
                                enabled = true,
                                shape = RoundedCornerShape(4.dp),
                                highlight = PlaceholderDefaults.shimmer
                            )
                    )
                }
            }
        }
        
        // Área principal de la cuadrícula con placeholder grande
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            // Grid structure similar a TimetableGrid
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Columna de horas (60dp de ancho) - siempre visible
                Column(
                    modifier = Modifier.width(60.dp)
                ) {
                    // Horas reales (00:00 a 23:00)
                    hours.forEach { hour ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
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
                        
                        // Divider
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Color(0xFFE0E0E0))
                        )
                    }
                }
                
                // Área de la cuadrícula con placeholder grande animado
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    // Placeholder grande que cubre TODA el área de la cuadrícula con animación shimmer
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 2.dp, vertical = 1.dp)
                            .placeholder(
                                enabled = true,
                                shape = RoundedCornerShape(8.dp),
                                highlight = PlaceholderDefaults.shimmer
                            )
                    )
                }
            }
        }
    }
}

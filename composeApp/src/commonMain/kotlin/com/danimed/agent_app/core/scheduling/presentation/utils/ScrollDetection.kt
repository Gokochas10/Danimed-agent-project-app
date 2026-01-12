package com.danimed.agent_app.core.scheduling.presentation.utils

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * Hook profesional para detectar la dirección del scroll y calcular la visibilidad del header.
 * 
 * Detecta la dirección del scroll comparando el estado actual con el anterior,
 * y calcula un alpha progresivo basado en la cantidad de scroll.
 * 
 * @param listState El estado del LazyList que se está observando
 * @param isEmpty Si la lista está vacía, el header siempre será visible
 * @param threshold Píxeles de scroll antes de ocultar completamente el header (default: 100)
 * @return Float entre 0f (oculto) y 1f (visible) representando la opacidad del header
 */
@Composable
fun rememberHeaderAlpha(
    listState: LazyListState,
    isEmpty: Boolean = false,
    threshold: Int = 100
): Float {
    var alpha by remember { mutableStateOf(1f) }
    var previousIndex by remember { mutableStateOf(0) }
    var previousOffset by remember { mutableStateOf(0) }
    var scrollDirection by remember { mutableStateOf<ScrollDirection?>(null) }
    
    // Observar cambios en el scroll usando snapshotFlow para mejor rendimiento
    LaunchedEffect(listState) {
        snapshotFlow { 
            Pair(listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset)
        }
        .distinctUntilChanged()
        .collect { (currentIndex, currentOffset) ->
            // Si no hay contenido, siempre mostrar el header
            if (isEmpty && currentIndex == 0 && currentOffset == 0) {
                alpha = 1f
                previousIndex = currentIndex
                previousOffset = currentOffset
                scrollDirection = null
                return@collect
            }
            
            // Detectar dirección del scroll
            val newDirection = when {
                currentIndex < previousIndex -> ScrollDirection.UP
                currentIndex > previousIndex -> ScrollDirection.DOWN
                currentOffset < previousOffset -> ScrollDirection.UP
                currentOffset > previousOffset -> ScrollDirection.DOWN
                else -> scrollDirection // Mantener dirección anterior
            }
            
            // Si la dirección cambió, mantener el alpha actual momentáneamente
            if (newDirection != scrollDirection && scrollDirection != null) {
                // Pequeño delay para evitar cambios bruscos
            }
            
            scrollDirection = newDirection
            
            // Calcular el total de scroll (aproximado)
            val totalScroll = currentOffset + (currentIndex * 500) // Aproximación
            
            // Calcular alpha basado en la dirección y cantidad de scroll
            alpha = when {
                // Al inicio (top), siempre visible
                currentIndex == 0 && currentOffset == 0 -> 1f
                // Scrolling up - desaparecer progresivamente
                newDirection == ScrollDirection.UP -> {
                    val progress = (totalScroll.coerceIn(0, threshold).toFloat() / threshold)
                    (1f - progress).coerceIn(0f, 1f)
                }
                // Scrolling down - aparecer progresivamente
                newDirection == ScrollDirection.DOWN -> {
                    val progress = (totalScroll.coerceIn(0, threshold).toFloat() / threshold)
                    (1f - progress).coerceIn(0f, 1f)
                }
                // Sin movimiento, mantener alpha actual
                else -> alpha
            }
            
            previousIndex = currentIndex
            previousOffset = currentOffset
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












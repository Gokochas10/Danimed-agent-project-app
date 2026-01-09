package com.danimed.agent_app.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.revenuecat.placeholder.PlaceholderDefaults
import com.revenuecat.placeholder.placeholder

/**
 * Placeholder para WeekSelector mientras se carga la fecha del servidor
 */
@Composable
fun WeekSelectorPlaceholder(
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFF7F9FC))
            .padding(horizontal = 16.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(7) {
            WeekDayPlaceholder()
        }
    }
}

@Composable
private fun WeekDayPlaceholder() {
    Box(
        modifier = Modifier
            .width(50.dp)
            .height(60.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFE0E0E0))
                .placeholder(
                    enabled = true,
                    shape = RoundedCornerShape(12.dp),
                    highlight = PlaceholderDefaults.shimmer
                )
        )
    }
}




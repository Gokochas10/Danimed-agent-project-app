package com.danimed.agent_app.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
 * Placeholder para DateHeader mientras se carga la fecha del servidor
 */
@Composable
fun DateHeaderPlaceholder(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFEFF4FF))
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(200.dp)
                .height(20.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xFFE0E0E0))
                .placeholder(
                    enabled = true,
                    shape = RoundedCornerShape(4.dp),
                    highlight = PlaceholderDefaults.shimmer
                )
        )
    }
}




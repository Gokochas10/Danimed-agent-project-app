package com.danimed.agent_app.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.revenuecat.placeholder.PlaceholderDefaults
import com.revenuecat.placeholder.placeholder

/**
 * Componente placeholder que simula las tarjetas de eventos de booking
 * mientras se carga la información desde el servidor.
 */
@Composable
fun BookingEventsPlaceholder(
    modifier: Modifier = Modifier,
    itemCount: Int = 3
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(itemCount) {
            BookingEventCardPlaceholder()
        }
    }
}

@Composable
private fun BookingEventCardPlaceholder() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .placeholder(
                enabled = true,
                shape = RoundedCornerShape(12.dp),
                highlight = PlaceholderDefaults.shimmer
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFFFF)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Status and date row placeholder
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFE0E0E0))
                        .placeholder(
                            enabled = true,
                            shape = RoundedCornerShape(4.dp),
                            highlight = PlaceholderDefaults.shimmer
                        )
                )
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(12.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFE0E0E0))
                        .placeholder(
                            enabled = true,
                            shape = RoundedCornerShape(4.dp),
                            highlight = PlaceholderDefaults.shimmer
                        )
                )
            }

            // Reason placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(12.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFE0E0E0))
                    .placeholder(
                        enabled = true,
                        shape = RoundedCornerShape(4.dp),
                        highlight = PlaceholderDefaults.shimmer
                    )
            )

            // Notes placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(12.dp)
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
}


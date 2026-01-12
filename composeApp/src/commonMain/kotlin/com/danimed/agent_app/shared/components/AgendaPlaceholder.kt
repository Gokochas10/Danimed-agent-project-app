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
 * Componente placeholder que simula las tarjetas de agenda
 * mientras se carga la información desde el servidor.
 */
@Composable
fun AgendaPlaceholder(
    modifier: Modifier = Modifier,
    itemCount: Int = 3
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FC)),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 20.dp,
            bottom = 100.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(itemCount) {
            AgendaCardPlaceholder()
        }
    }
}

@Composable
fun AgendaCardPlaceholder() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .placeholder(
                enabled = true,
                shape = RoundedCornerShape(16.dp),
                highlight = PlaceholderDefaults.shimmer
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFFFF)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header row placeholder
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(20.dp)
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
                        .width(80.dp)
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
            
            // Title placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(24.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFE0E0E0))
                    .placeholder(
                        enabled = true,
                        shape = RoundedCornerShape(4.dp),
                        highlight = PlaceholderDefaults.shimmer
                    )
            )
            
            // Time placeholder
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(16.dp)
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


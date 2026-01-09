package com.danimed.agent_app.core.searching.presentation.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danimed.agent_app.core.searching.application.viewModel.SearchViewModel
import com.danimed.agent_app.core.searching.domain.model.SearchQuery
import com.danimed.agent_app.shared.di.SearchModule
import com.danimed.agent_app.shared.theme.InterFontFamily
import com.danimed.agent_app.shared.theme.LoginBackground
import com.danimed.agent_app.shared.theme.PrimaryBlue
import com.danimed.agent_app.shared.theme.White
import compose.icons.FeatherIcons
import compose.icons.feathericons.Search
import compose.icons.feathericons.X

@Composable
fun SearchScreen(
    onDismiss: () -> Unit,
    initialClickPosition: Offset? = null,
    modifier: Modifier = Modifier
) {
    val viewModel = remember {
        SearchViewModel(
            SearchModule.getRecentSearchesUseCase,
            SearchModule.saveRecentSearchUseCase,
            SearchModule.clearRecentSearchesUseCase
        )
    }
    val uiState by viewModel.uiState

    // Animation state - using scale and alpha for a smooth entrance
    val scale = remember { Animatable(0.8f) }
    val alpha = remember { Animatable(0f) }

    // Start animation when screen appears
    LaunchedEffect(Unit) {
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 300, easing = EaseOutCubic)
        )
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 400, easing = EaseOutCubic)
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .alpha(alpha.value)
                .scale(scale.value)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Header with close button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Buscar",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = InterFontFamily(),
                    color = PrimaryBlue
                )

                Icon(
                    painter = rememberVectorPainter(image = FeatherIcons.X),
                    contentDescription = "Cerrar",
                    tint = PrimaryBlue,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onDismiss() }
                )
            }

            // Search input
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                placeholder = {
                    Text(
                        text = "Buscar paciente por nombre o cédula...",
                        fontFamily = InterFontFamily(),
                        fontSize = 12.sp,
                        color = PrimaryBlue.copy(alpha = 0.6f)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        painter = rememberVectorPainter(image = FeatherIcons.Search),
                        contentDescription = "Buscar",
                        tint = PrimaryBlue.copy(alpha = 0.7f)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        if (uiState.searchQuery.isNotBlank()) {
                            viewModel.saveSearch(uiState.searchQuery)
                        }
                    }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = PrimaryBlue,
                    unfocusedTextColor = PrimaryBlue,
                    focusedPlaceholderColor = PrimaryBlue.copy(alpha = 0.6f),
                    unfocusedPlaceholderColor = PrimaryBlue.copy(alpha = 0.6f),
                    focusedBorderColor = PrimaryBlue,
                    unfocusedBorderColor = PrimaryBlue.copy(alpha = 0.5f),
                    focusedContainerColor = White,
                    unfocusedContainerColor = White
                ),
                shape = RoundedCornerShape(12.dp)
            )

            // Recent Searches section
            if (uiState.recentSearches.isNotEmpty()) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Búsquedas recientes",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = InterFontFamily(),
                            color = PrimaryBlue
                        )

                        Text(
                            text = "Limpiar todo",
                            fontSize = 14.sp,
                            fontFamily = InterFontFamily(),
                            color = PrimaryBlue.copy(alpha = 0.7f),
                            modifier = Modifier.clickable {
                                viewModel.clearRecentSearches()
                            }
                        )
                    }

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.recentSearches) { search ->
                            RecentSearchItem(
                                query = search.query,
                                onClick = {
                                    viewModel.updateSearchQuery(search.query)
                                    viewModel.saveSearch(search.query)
                                }
                            )
                        }
                    }
                }
            } else {
                // Empty state
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay búsquedas recientes",
                        fontSize = 14.sp,
                        fontFamily = InterFontFamily(),
                        color = PrimaryBlue.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@Composable
private fun RecentSearchItem(
    query: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = LoginBackground,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            painter = rememberVectorPainter(image = FeatherIcons.Search),
            contentDescription = null,
            tint = PrimaryBlue.copy(alpha = 0.7f),
            modifier = Modifier.size(16.dp)
        )

        Text(
            text = query,
            fontSize = 14.sp,
            fontFamily = InterFontFamily(),
            color = PrimaryBlue,
            modifier = Modifier.weight(1f)
        )
    }
}


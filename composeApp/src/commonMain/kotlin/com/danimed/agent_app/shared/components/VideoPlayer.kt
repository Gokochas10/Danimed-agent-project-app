package com.danimed.agent_app.shared.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun VideoPlayer(
    resource: String,
    modifier: Modifier = Modifier,
    looping: Boolean = false,
    key: Int = 0
)


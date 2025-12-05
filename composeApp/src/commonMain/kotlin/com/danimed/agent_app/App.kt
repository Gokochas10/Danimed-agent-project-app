package com.danimed.agent_app

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.danimed.agent_app.shared.navigation.AuthMiddleware
import com.danimed.agent_app.shared.navigation.Screen
import com.danimed.agent_app.shared.theme.AppTheme

@Composable
@Preview
fun App() {
    AppTheme {
        AuthMiddleware { screen ->
            when (screen) {
                is Screen.Home -> {
                }
                else -> {}
            }
        }
    }
}
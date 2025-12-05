package com.danimed.agent_app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.danimed.agent_app.core.feat1.presentation.screens.LoginScreen
import com.danimed.agent_app.core.feat1.presentation.screens.SplashScreen
import com.danimed.agent_app.shared.theme.AppTheme
import kotlinx.coroutines.delay

@Composable
@Preview
fun App() {
    AppTheme {
        var showLogin by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            delay(3000)
            showLogin = true
        }

        if (showLogin) {
            LoginScreen()
        } else {
            SplashScreen()
        }
    }
}
package com.danimed.agent_app.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danimed.agent_app.shared.theme.InterFontFamily
import com.danimed.agent_app.shared.theme.SplashBackground
import com.danimed.agent_app.shared.theme.White
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun NoInternetScreen(
    onRetry: () -> Unit,
    checkInternetConnection: suspend () -> Boolean,
    videoKey: Int = 0,
    modifier: Modifier = Modifier
) {
    val fontFamily = InterFontFamily()
    val scope = rememberCoroutineScope()
    var isChecking by remember { mutableStateOf(false) }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(White)
    ) {
        // Tamaño de pantalla multiplatform
        val screenWidth = maxWidth
        val screenHeight = maxHeight

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .offset(y = -(screenHeight * 0.08f)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "¡UPS!",
                fontSize = (screenWidth.value * 0.22f).sp,
                fontWeight = FontWeight.W800,
                fontFamily = fontFamily,
                color = SplashBackground,
                textAlign = TextAlign.Center
            )


            Box(
                modifier = Modifier.size(screenWidth * 0.9f),
                contentAlignment = Alignment.Center
            ) {
                VideoPlayer(
                    resource = "no_connection",
                    modifier = Modifier.fillMaxSize(),
                    looping = true,
                    key = videoKey
                )
            }

            Spacer(modifier = Modifier.height(screenHeight * 0.015f))

            Text(
                text = "Parece que no tienes conexión.\nInténtalo nuevamente.",
                fontSize = (screenWidth.value * 0.045f).sp,
                fontWeight = FontWeight.Normal,
                fontFamily = fontFamily,
                color = SplashBackground.copy(alpha = 0.75f),
                textAlign = TextAlign.Center,
                lineHeight = (screenWidth.value * 0.06f).sp
            )
        }

        Button(
            onClick = {
                scope.launch {
                    isChecking = true
                    delay(500)
                    val hasInternet = checkInternetConnection()
                    isChecking = false
                    if (hasInternet) onRetry()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = screenWidth * 0.08f)
                .padding(bottom = screenHeight * 0.07f)
                .height(screenHeight * 0.07f)
                .align(Alignment.BottomCenter),
            colors = ButtonDefaults.buttonColors(
                containerColor = SplashBackground
            ),
            shape = RoundedCornerShape(16.dp),
            enabled = !isChecking
        ) {
            if (isChecking) {
                CircularProgressIndicator(
                    modifier = Modifier.size(screenHeight * 0.03f),
                    color = White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Reintentar",
                    fontSize = (screenWidth.value * 0.045f).sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = fontFamily,
                    color = White
                )
            }
        }
    }
}





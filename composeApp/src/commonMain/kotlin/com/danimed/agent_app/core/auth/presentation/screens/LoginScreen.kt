package com.danimed.agent_app.core.auth.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.clickable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.resources.painterResource
import agent_app.composeapp.generated.resources.Res
import agent_app.composeapp.generated.resources.danimed_logo
import com.danimed.agent_app.core.auth.application.viewModel.LoginViewModel
import com.danimed.agent_app.getPlatform
import com.danimed.agent_app.shared.di.AuthModule
import com.danimed.agent_app.shared.theme.InterFontFamily
import com.danimed.agent_app.shared.theme.LoginBackground
import com.danimed.agent_app.shared.theme.PrimaryBlue
import com.danimed.agent_app.shared.theme.SplashBackground
import com.danimed.agent_app.shared.theme.White
import com.danimed.agent_app.shared.utils.CredentialsManagerProvider
import com.danimed.agent_app.shared.utils.FcmTokenManagerProvider

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit = { _ -> }
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    val fontFamily = InterFontFamily()
    val credentialsManager = remember { CredentialsManagerProvider.getCredentialsManager() }
    val fcmTokenManager = remember { FcmTokenManagerProvider.getFcmTokenManager() }
    val platform = remember { 
        val platformName = getPlatform().name.lowercase()
        if (platformName.contains("android")) "android" else "ios"
    }
    val viewModel: LoginViewModel = viewModel {
        LoginViewModel(AuthModule.loginUseCase)
    }
    
    // Cargar credenciales guardadas si existen
    LaunchedEffect(Unit) {
        val savedCredentials = credentialsManager.getCredentials()
        if (savedCredentials != null) {
            username = savedCredentials.first
            password = savedCredentials.second
            rememberMe = true
        }
    }

    LaunchedEffect(viewModel.uiState.isSuccess, viewModel.uiState.token) {
        if (viewModel.uiState.isSuccess && viewModel.uiState.token != null) {
            // Guardar credenciales si "mantener inicio de sesión" está activado
            if (rememberMe) {
                credentialsManager.saveCredentials(username, password)
            } else {
                credentialsManager.clearCredentials()
            }
            onLoginSuccess(viewModel.uiState.token!!)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.30f)
                .background(
                    color = SplashBackground,
                    shape = RoundedCornerShape(bottomEnd = 70.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.danimed_logo),
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(White)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .fillMaxWidth(0.5f)
                    .height(80.dp)
                    .background(
                        color = SplashBackground,
                        shape = RoundedCornerShape(bottomEnd = 50.dp, topEnd = 0.dp, bottomStart = 0.dp, topStart = 0.dp)
                    )
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = White,
                        shape = RoundedCornerShape(topStart = 70.dp)
                    )
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Iniciar Sesión",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue,
                        fontFamily = fontFamily,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    if (viewModel.uiState.error != null) {
                        Text(
                            text = viewModel.uiState.error ?: "",
                            color = androidx.compose.ui.graphics.Color.Red,
                            fontSize = 14.sp,
                            fontFamily = fontFamily,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }

                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Usuario/CI", fontFamily = fontFamily) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !viewModel.uiState.isLoading,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = PrimaryBlue,
                            unfocusedTextColor = PrimaryBlue,
                            focusedLabelColor = PrimaryBlue,
                            unfocusedLabelColor = PrimaryBlue.copy(alpha = 0.6f),
                            focusedBorderColor = PrimaryBlue,
                            unfocusedBorderColor = PrimaryBlue.copy(alpha = 0.5f),
                            focusedContainerColor = LoginBackground,
                            unfocusedContainerColor = LoginBackground
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña", fontFamily = fontFamily) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        enabled = !viewModel.uiState.isLoading,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = PrimaryBlue,
                            unfocusedTextColor = PrimaryBlue,
                            focusedLabelColor = PrimaryBlue,
                            unfocusedLabelColor = PrimaryBlue.copy(alpha = 0.6f),
                            focusedBorderColor = PrimaryBlue,
                            unfocusedBorderColor = PrimaryBlue.copy(alpha = 0.5f),
                            focusedContainerColor = LoginBackground,
                            unfocusedContainerColor = LoginBackground
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Switch para mantener inicio de sesión
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Start
                    ) {
                        Switch(
                            checked = rememberMe,
                            onCheckedChange = { rememberMe = it },
                            enabled = !viewModel.uiState.isLoading
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Mantener inicio de sesión",
                            fontSize = 14.sp,
                            fontFamily = fontFamily,
                            color = PrimaryBlue,
                            modifier = Modifier.clickable(enabled = !viewModel.uiState.isLoading) {
                                rememberMe = !rememberMe
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    val isEnabled =
                        !viewModel.uiState.isLoading &&
                                username.isNotBlank() &&
                                password.isNotBlank()

                    Button(
                        onClick = {
                            val fcmToken = fcmTokenManager.getToken()
                            viewModel.login(
                                username = username,
                                password = password,
                                fcmToken = fcmToken,
                                platform = platform,
                                onSuccess = onLoginSuccess
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = isEnabled,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryBlue
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        if (viewModel.uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = White
                            )
                        } else {
                            Text(
                                text = "Iniciar Sesión",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = fontFamily,
                                color = if (isEnabled) {
                                    White
                                } else {
                                    SplashBackground.copy(alpha = 0.4f)
                                }
                            )
                        }
                    }
                }
            }
        }

    }
}
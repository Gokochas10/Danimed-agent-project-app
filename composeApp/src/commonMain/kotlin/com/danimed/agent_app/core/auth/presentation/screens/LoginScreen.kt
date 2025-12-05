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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.danimed.agent_app.shared.di.AuthModule
import com.danimed.agent_app.shared.theme.InterFontFamily
import com.danimed.agent_app.shared.theme.LoginBackground
import com.danimed.agent_app.shared.theme.PrimaryBlue
import com.danimed.agent_app.shared.theme.SplashBackground
import com.danimed.agent_app.shared.theme.White

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit = { _ -> }
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val fontFamily = InterFontFamily()
    val viewModel: LoginViewModel = viewModel {
        LoginViewModel(AuthModule.loginUseCase)
    }

    LaunchedEffect(viewModel.uiState.isSuccess, viewModel.uiState.token) {
        if (viewModel.uiState.isSuccess && viewModel.uiState.token != null) {
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
                    .fillMaxHeight()
                    .background(
                        color = White,
                        shape = RoundedCornerShape(topStart = 70.dp)
                    )
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp),
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

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { viewModel.login(username, password, onLoginSuccess) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = !viewModel.uiState.isLoading && username.isNotBlank() && password.isNotBlank(),
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
                                color = White
                            )
                        }
                    }
                }
            }
        }
    }
}


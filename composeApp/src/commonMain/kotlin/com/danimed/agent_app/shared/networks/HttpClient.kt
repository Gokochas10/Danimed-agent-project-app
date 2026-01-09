package com.danimed.agent_app.shared.networks

import com.danimed.agent_app.shared.navigation.AuthRedirectHandler
import com.danimed.agent_app.shared.utils.TokenManagerProvider
import io.ktor.client.HttpClient
import io.ktor.client.call.HttpClientCall
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.statement.HttpReceivePipeline
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createHttpClient(): HttpClient {
    return HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                encodeDefaults = false
            })
        }

        install(Logging) {
            level = LogLevel.ALL
        }
        
        install(HttpCallValidator) {
            validateResponse { response ->
                when {
                    response.status == HttpStatusCode.Unauthorized -> {
                        // No limpiar el token aquí, dejar que App.kt maneje el re-login automático
                        AuthRedirectHandler.notifyUnauthorized()
                    }
                    response.status.value >= 500 -> {
                        // Error del servidor (servidor caído, error interno)
                        NetworkErrorHandler.handleNetworkError(
                            NetworkError.ServerError(
                                response.status.value,
                                "Error del servidor: ${response.status}"
                            )
                        )
                    }
                    response.status.value in 400..499 && response.status != HttpStatusCode.Unauthorized -> {
                        // Errores del cliente (URL incorrecta, recurso no encontrado, etc.)
                        // Estos son errores de servidor/configuración, no falta de internet
                        NetworkErrorHandler.handleNetworkError(
                            NetworkError.ServerError(
                                response.status.value,
                                "Error en la petición: ${response.status}"
                            )
                        )
                    }
                }
            }
            
            // Manejar excepciones de respuesta (errores de conexión, timeouts, etc.)
            handleResponseException { cause ->
                // Convertir la excepción a NetworkError y manejarla
                val networkError = cause.toNetworkError()
                when (networkError) {
                    is NetworkError.NoConnection -> {
                        // Error de conexión (no hay internet)
                        NetworkErrorHandler.handleNetworkError(networkError)
                    }
                    is NetworkError.ServerError -> {
                        // Error de servidor que no fue capturado por validateResponse
                        NetworkErrorHandler.handleNetworkError(networkError)
                    }
                    else -> {
                        // Otros errores no se manejan aquí
                    }
                }
                // No relanzar la excepción aquí - se maneja en el código que hace la llamada
            }
        }
    }.apply {
        requestPipeline.intercept(HttpRequestPipeline.Transform) {
            val url = context.url.toString()
            val isAuthEndpoint = url.contains("/auth/login") || url.contains("/auth/register")
            
            if (!isAuthEndpoint) {
                val token = TokenManagerProvider.getTokenManager().getToken()
                if (token != null && !context.headers.contains(HttpHeaders.Authorization)) {
                    context.headers[HttpHeaders.Authorization] = "Bearer $token"
                }
            }
        }
    }
}


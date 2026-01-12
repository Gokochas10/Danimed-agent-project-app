package com.danimed.agent_app.shared.networks

sealed class NetworkError : Exception() {
    data class Unauthorized(override val message: String = "Token inválido o expirado") : NetworkError()
    data class NoConnection(override val message: String = "Sin conexión a internet") : NetworkError()
    data class ServerError(val code: Int, override val message: String) : NetworkError()
    data class Unknown(override val message: String, override val cause: Throwable? = null) : NetworkError()
}

fun Throwable.toNetworkError(): NetworkError {
    val error = when {
        this is NetworkError -> this
        // Errores que definitivamente indican falta de conexión a internet
        // (no se puede resolver el host, no hay red disponible)
        message?.contains("Unable to resolve host", ignoreCase = true) == true ||
        message?.contains("No address associated with hostname", ignoreCase = true) == true ||
        message?.contains("Network is unreachable", ignoreCase = true) == true ||
        message?.contains("Network unreachable", ignoreCase = true) == true ||
        message?.contains("No route to host", ignoreCase = true) == true -> {
            NetworkError.NoConnection()
        }
        // Errores que pueden ser tanto falta de internet como servidor caído
        // Estos se clasificarán correctamente en NetworkErrorHandler usando NetworkConnectivityManager
        message?.contains("Failed to connect", ignoreCase = true) == true ||
        message?.contains("Connection refused", ignoreCase = true) == true ||
        message?.contains("Connection timed out", ignoreCase = true) == true ||
        message?.contains("Network error", ignoreCase = true) == true ||
        (message?.contains("SocketTimeoutException", ignoreCase = true) == true) ||
        (message?.contains("ConnectTimeoutException", ignoreCase = true) == true) -> {
            // Clasificar como Unknown primero, luego NetworkErrorHandler verificará la conexión
            // y lo clasificará correctamente como NoConnection o ServerError
            NetworkError.Unknown(message ?: "Error de conexión", this)
        }
        // Errores de servidor (servidor caído, URL incorrecta, etc.) - estos son errores HTTP
        message?.contains("HTTP", ignoreCase = true) == true ||
        message?.contains("Bad Request", ignoreCase = true) == true ||
        message?.contains("Not Found", ignoreCase = true) == true ||
        message?.contains("Internal Server Error", ignoreCase = true) == true ||
        message?.contains("Service Unavailable", ignoreCase = true) == true ||
        message?.contains("Gateway Timeout", ignoreCase = true) == true ||
        message?.contains("Bad Gateway", ignoreCase = true) == true -> {
            NetworkError.ServerError(0, message ?: "Error del servidor")
        }
        else -> NetworkError.Unknown(message ?: "Error desconocido", this)
    }
    
    // NO notificar al handler automáticamente aquí
    // El handler se llama desde HttpCallValidator.handleResponseException
    // para evitar doble manejo
    
    return error
}


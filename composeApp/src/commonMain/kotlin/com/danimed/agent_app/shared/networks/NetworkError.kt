package com.danimed.agent_app.shared.networks

sealed class NetworkError : Exception() {
    data class Unauthorized(override val message: String = "Token inválido o expirado") : NetworkError()
    data class NoConnection(override val message: String = "Sin conexión a internet") : NetworkError()
    data class ServerError(val code: Int, override val message: String) : NetworkError()
    data class Unknown(override val message: String, override val cause: Throwable? = null) : NetworkError()
}

fun Throwable.toNetworkError(): NetworkError {
    return when {
        this is NetworkError -> this
        message?.contains("Unable to resolve host", ignoreCase = true) == true ||
        message?.contains("Failed to connect", ignoreCase = true) == true ||
        message?.contains("Network is unreachable", ignoreCase = true) == true -> {
            NetworkError.NoConnection()
        }
        else -> NetworkError.Unknown(message ?: "Error desconocido", this)
    }
}


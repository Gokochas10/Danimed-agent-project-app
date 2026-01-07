package com.danimed.agent_app.shared.utils

import kotlinx.datetime.LocalDate

/**
 * Función multiplataforma para obtener la fecha local actual.
 * La implementación concreta se hace en cada plataforma.
 */
expect fun currentLocalDate(): LocalDate





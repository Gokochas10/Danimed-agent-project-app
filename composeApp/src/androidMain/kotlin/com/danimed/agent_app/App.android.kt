package com.danimed.agent_app

import com.danimed.agent_app.shared.components.VideoPreloader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

actual suspend fun preloadVideoIfNeeded(resource: String) {
    // Esta función no puede acceder al contexto aquí
    // La precarga se hace en MainActivity donde tenemos acceso al contexto
}








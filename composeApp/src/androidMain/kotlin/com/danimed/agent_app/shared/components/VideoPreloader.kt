package com.danimed.agent_app.shared.components

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.ExperimentalResourceApi
import agent_app.composeapp.generated.resources.Res
import java.io.File

/**
 * VideoPreloader específico de Android. Se ha movido a androidMain para
 * que no bloquee la compilación en iOS.
 */
object VideoPreloader {
    private val preloadedVideos = mutableMapOf<String, File>()

    @OptIn(ExperimentalResourceApi::class)
    suspend fun preloadVideo(context: Context, resource: String) {
        withContext(Dispatchers.IO) {
            try {
                val normalizedResource = resource.lowercase()
                    .replace("-", "_")
                    .replace(" ", "_")

                // Si ya está precargado, no hacer nada
                if (preloadedVideos.containsKey(normalizedResource)) {
                    return@withContext
                }

                // Intentar desde drawable/ primero
                val resourcePath = try {
                    Res.readBytes("drawable/${normalizedResource}.mp4")
                    "drawable/${normalizedResource}.mp4"
                } catch (e: Exception) {
                    try {
                        Res.readBytes("files/${normalizedResource}.mp4")
                        "files/${normalizedResource}.mp4"
                    } catch (e2: Exception) {
                        android.util.Log.e("VideoPreloader", "Resource not found", e2)
                        null
                    }
                }

                if (resourcePath != null) {
                    val videoBytes = Res.readBytes(resourcePath)
                    val tempFile = File(context.cacheDir, "${normalizedResource}.mp4")

                    if (!tempFile.exists() || tempFile.length() != videoBytes.size.toLong()) {
                        tempFile.outputStream().use { output ->
                            output.write(videoBytes)
                        }
                    }

                    preloadedVideos[normalizedResource] = tempFile
                    android.util.Log.d("VideoPreloader", "Video precargado: $normalizedResource")
                }
            } catch (e: Exception) {
                android.util.Log.e("VideoPreloader", "Error preloading video", e)
            }
        }
    }

    fun getPreloadedVideo(resource: String): File? {
        val normalizedResource = resource.lowercase()
            .replace("-", "_")
            .replace(" ", "_")
        return preloadedVideos[normalizedResource]
    }

    fun clearCache() {
        preloadedVideos.clear()
    }
}







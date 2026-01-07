package com.danimed.agent_app.shared.components

import android.media.MediaPlayer
import android.net.Uri
import android.view.SurfaceView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.ExperimentalResourceApi
import agent_app.composeapp.generated.resources.Res
import java.io.File

@OptIn(ExperimentalResourceApi::class)
@Composable
actual fun VideoPlayer(
    resource: String,
    modifier: Modifier,
    looping: Boolean,
    key: Int
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var mediaPlayer by remember(key) { mutableStateOf<MediaPlayer?>(null) }
    var videoFile by remember(key) { mutableStateOf<File?>(null) }
    var isVideoReady by remember(key) { mutableStateOf(false) }
    var isVideoPlaying by remember(key) { mutableStateOf(false) }

    // Normalizar el nombre del recurso
    val normalizedResource = resource.lowercase()
        .replace("-", "_")
        .replace(" ", "_")

    // Cargar el video (usar precargado si existe)
    LaunchedEffect(normalizedResource, key) {
        isVideoReady = false
        isVideoPlaying = false
        // Intentar obtener video precargado primero
        val preloaded = VideoPreloader.getPreloadedVideo(resource)
        if (preloaded != null && preloaded.exists()) {
            videoFile = preloaded
            isVideoReady = true
            return@LaunchedEffect
        }
        
        // Si no está precargado, cargarlo normalmente
        withContext(Dispatchers.IO) {
            try {
                val resourcePath = try {
                    Res.readBytes("drawable/${normalizedResource}.mp4")
                    "drawable/${normalizedResource}.mp4"
                } catch (e: Exception) {
                    try {
                        Res.readBytes("files/${normalizedResource}.mp4")
                        "files/${normalizedResource}.mp4"
                    } catch (e2: Exception) {
                        android.util.Log.e("VideoPlayer", "Resource not found in drawable or files", e2)
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

                    withContext(Dispatchers.Main) {
                        videoFile = tempFile
                        isVideoReady = true
                    }
                }
            } catch (e: Exception) {
                android.util.Log.e(
                    "VideoPlayer",
                    "Error loading video: ${e.message}",
                    e
                )
            }
        }
    }

    DisposableEffect(videoFile, key) {
        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Mostrar el AndroidView cuando el archivo esté listo
        // El SurfaceView necesita estar visible para que se cree la superficie
        if (videoFile != null && isVideoReady) {
            AndroidView(
                factory = { ctx ->
                    SurfaceView(ctx).also { surfaceView ->
                        // Configurar el callback del SurfaceHolder
                        surfaceView.holder.addCallback(object : android.view.SurfaceHolder.Callback {
                            override fun surfaceCreated(holder: android.view.SurfaceHolder) {
                                try {
                                    // Liberar el MediaPlayer anterior si existe
                                    mediaPlayer?.release()
                                    
                                    // Crear nuevo MediaPlayer
                                    val mp = MediaPlayer().apply {
                                        setDisplay(holder)
                                        isLooping = looping
                                        setVolume(0f, 0f)

                                        setOnPreparedListener {
                                            start()
                                            // Marcar como reproduciéndose cuando realmente empieza
                                            isVideoPlaying = true
                                        }

                                        setOnErrorListener { _, what, extra ->
                                            android.util.Log.e(
                                                "VideoPlayer",
                                                "Error: what=$what, extra=$extra"
                                            )
                                            isVideoPlaying = false
                                            false
                                        }
                                    }

                                    mediaPlayer = mp

                                    // Cargar el video desde el archivo temporal
                                    scope.launch(Dispatchers.IO) {
                                        try {
                                            val fileUri = Uri.fromFile(videoFile)
                                            mp.setDataSource(ctx, fileUri)

                                            withContext(Dispatchers.Main) {
                                                mp.prepareAsync()
                                            }
                                        } catch (e: Exception) {
                                            android.util.Log.e(
                                                "VideoPlayer",
                                                "Error setting data source: ${e.message}",
                                                e
                                            )
                                            withContext(Dispatchers.Main) {
                                                isVideoPlaying = false
                                            }
                                        }
                                    }
                                } catch (e: Exception) {
                                    android.util.Log.e(
                                        "VideoPlayer",
                                        "Error creating MediaPlayer: ${e.message}",
                                        e
                                    )
                                    isVideoPlaying = false
                                }
                            }

                            override fun surfaceChanged(
                                holder: android.view.SurfaceHolder,
                                format: Int,
                                width: Int,
                                height: Int
                            ) {
                                // No necesita implementación
                            }

                            override fun surfaceDestroyed(holder: android.view.SurfaceHolder) {
                                mediaPlayer?.release()
                                mediaPlayer = null
                                isVideoPlaying = false
                            }
                        })
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(if (isVideoPlaying) 1f else 0f),
                update = { view ->
                    // El update se llama en cada recomposición
                    // Actualizar la alpha del SurfaceView basado en isVideoPlaying
                    view.alpha = if (isVideoPlaying) 1f else 0f
                }
            )
        }
    }
}





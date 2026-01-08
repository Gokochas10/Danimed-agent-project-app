package com.danimed.agent_app.shared.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItem
import platform.AVFoundation.play
import platform.AVFoundation.pause
import platform.AVKit.AVPlayerViewController
import platform.Foundation.NSBundle
import platform.Foundation.NSURL
import platform.UIKit.UIView

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun VideoPlayer(
    resource: String,
    modifier: Modifier,
    looping: Boolean,
    key: Int
) {
    val player = remember(key) { 
        // Obtener la ruta del recurso desde el bundle
        val bundle = NSBundle.mainBundle
        val resourceName = resource.lowercase().replace("-", "_").replace(" ", "_")
        val resourcePath = bundle.pathForResource(resourceName, "mp4")
        
        if (resourcePath != null) {
            val url = NSURL.fileURLWithPath(resourcePath)
            // Por ahora usamos un AVPlayer simple. Si se necesita loop explícito,
            // se puede añadir un observer para reiniciar al finalizar.
            AVPlayer(uRL = url)
        } else {
            null
        }
    }
    
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (player != null) {
            UIKitView(
                factory = {
                    val playerViewController = AVPlayerViewController()
                    playerViewController.player = player
                    playerViewController.showsPlaybackControls = false
                    
                    val containerView = UIView()
                    containerView.addSubview(playerViewController.view)
                    containerView
                },
                modifier = Modifier.fillMaxSize(),
                update = { _ -> }
            )
            
            DisposableEffect(player) {
                player.play()
                
                onDispose {
                    player.pause()
                }
            }
        }
    }
}



package com.danimed.agent_app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.danimed.agent_app.shared.components.VideoPreloader
import com.danimed.agent_app.shared.notifications.NotificationManager
import com.danimed.agent_app.shared.utils.CredentialsManagerProvider
import com.danimed.agent_app.shared.utils.FcmTokenManagerProvider
import com.danimed.agent_app.shared.utils.NotificationsLocalManagerProvider
import com.danimed.agent_app.shared.utils.RecentSearchesManagerProvider
import com.danimed.agent_app.shared.utils.TokenManagerProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val notificationManager by lazy { NotificationManager(this) }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            println("✅ Permiso de notificaciones concedido")
            initializePushNotifications()
        } else {
            println("❌ Permiso de notificaciones denegado")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        TokenManagerProvider.init(this)
        RecentSearchesManagerProvider.init(this)
        CredentialsManagerProvider.init(this)
        FcmTokenManagerProvider.init(this)
        NotificationsLocalManagerProvider.init(this)

        checkAndRequestNotificationPermission()
        
        // Manejar notificaciones cuando la app se abre desde una notificación
        handleNotificationIntent(intent)

        setContent {
            // Precargar el video de no internet
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                GlobalScope.launch {
                    VideoPreloader.preloadVideo(context, "no_connection")
                }
            }
            
            App()
        }
    }

    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    println("✅ Ya tiene permisos de notificación")
                    initializePushNotifications()
                }
                else -> {
                    println("🔔 Solicitando permisos de notificación...")
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            // Android < 13 no requiere permiso en runtime
            initializePushNotifications()
        }
    }
    
    private fun initializePushNotifications() {
        val fcmTokenManager = FcmTokenManagerProvider.getFcmTokenManager()
        
        // Solo obtener el token si no existe ya guardado
        if (!fcmTokenManager.hasToken()) {
            notificationManager.initializePushNotifications { token ->
                println("🎯 TOKEN FCM obtenido: $token")
                // Guardar el token localmente (solo una vez al instalar)
                fcmTokenManager.saveToken(token)
                println("✅ Token FCM guardado localmente")
            }
        } else {
            println("ℹ️ Token FCM ya existe, no se necesita obtener nuevamente")
        }
    }
    
    override fun onResume() {
        super.onResume()
        // Actualizar contador cuando la app vuelve a foreground
        println("🔄 App en resume - actualizando contador de notificaciones")
    }
    
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // Manejar notificaciones cuando la app ya está abierta y se recibe una notificación
        handleNotificationIntent(intent)
    }
    
    private fun handleNotificationIntent(intent: Intent?) {
        // Si la app se abrió desde una notificación, asegurarse de que se procese
        intent?.extras?.let { extras ->
            println("📩 App abierta desde notificación")
            // Los datos de la notificación ya deberían estar guardados por MyFirebaseMessagingService
            // Solo necesitamos asegurarnos de que el contador se actualice
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
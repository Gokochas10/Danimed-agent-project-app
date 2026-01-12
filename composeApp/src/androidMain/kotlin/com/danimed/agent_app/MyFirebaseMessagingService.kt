package com.danimed.agent_app

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.danimed.agent_app.core.notifications.domain.model.Notification
import com.danimed.agent_app.shared.di.NotificationsModule
import com.danimed.agent_app.shared.utils.FcmTokenManagerProvider
import com.danimed.agent_app.shared.utils.NotificationsLocalManagerProvider
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.UUID

class MyFirebaseMessagingService : FirebaseMessagingService() {
    
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    
    init {
        // Inicializar providers con el contexto del servicio
        FcmTokenManagerProvider.init(this)
        NotificationsLocalManagerProvider.init(this)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        println("📩 Mensaje recibido desde: ${remoteMessage.from}")
        println("📩 Tiene notification: ${remoteMessage.notification != null}")
        println("📩 Tiene data: ${remoteMessage.data.isNotEmpty()}")

        // Extraer título y mensaje de notification o data
        val title = remoteMessage.notification?.title 
            ?: remoteMessage.data["title"] 
            ?: "Notificación"
        val message = remoteMessage.notification?.body 
            ?: remoteMessage.data["body"] 
            ?: remoteMessage.data["message"]
            ?: "Mensaje"
        
        println("📩 Título: $title, Mensaje: $message")
        
        // IMPORTANTE: onMessageReceived solo se llama cuando:
        // 1. La app está en foreground, O
        // 2. El payload contiene solo "data" (sin "notification")
        // 
        // Si el payload contiene "notification" y la app está en background,
        // FCM muestra la notificación automáticamente pero NO llama a onMessageReceived.
        // Para que siempre se guarde, el backend debe enviar un payload con "data" además de "notification"
        // Ejemplo:
        // {
        //   "notification": { "title": "...", "body": "..." },
        //   "data": { "title": "...", "body": "...", "message": "..." }
        // }
        
        // SIEMPRE guardar notificación localmente PRIMERO
        saveNotificationLocally(title, message, remoteMessage.data)
        
        // SIEMPRE mostrar notificación del sistema (incluso cuando la app está abierta)
        // Esto asegura que la notificación aparezca en el panel del sistema
        if (hasNotificationPermission()) {
            try {
                showNotification(
                    title = title,
                    message = message,
                    data = remoteMessage.data
                )
                println("✅ Notificación del sistema mostrada")
            } catch (e: SecurityException) {
                println("❌ Error de seguridad al mostrar notificación: ${e.message}")
            } catch (e: Exception) {
                println("❌ Error al mostrar notificación: ${e.message}")
                e.printStackTrace()
            }
        } else {
            println("⚠️ No hay permisos para mostrar notificaciones")
        }
    }
    
    
    private fun isAppInForeground(): Boolean {
        val activityManager = getSystemService(android.content.Context.ACTIVITY_SERVICE) as android.app.ActivityManager
        val appProcesses = activityManager.runningAppProcesses ?: return false
        val packageName = packageName
        for (appProcess in appProcesses) {
            if (appProcess.importance == android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                && appProcess.processName == packageName) {
                return true
            }
        }
        return false
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        println("Nuevo token FCM generado: $token")
        
        // Guardar el nuevo token localmente
        try {
            val fcmTokenManager = FcmTokenManagerProvider.getFcmTokenManager()
            fcmTokenManager.saveToken(token)
            println("Nuevo token FCM guardado localmente")
        } catch (e: Exception) {
            println("Error al guardar nuevo token FCM: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            // Android < 13 no requiere permiso en runtime
            true
        }
    }

    @RequiresPermission(value = "android.permission.POST_NOTIFICATIONS")
    private fun showNotification(
        title: String,
        message: String,
        data: Map<String, String>
    ) {
        val intent = packageManager.getLaunchIntentForPackage(packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        val notification = NotificationCompat.Builder(this, "backend_updates")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        
        NotificationManagerCompat.from(this)
            .notify(System.currentTimeMillis().toInt(), notification)
    }
    
    private fun saveNotificationLocally(
        title: String,
        message: String,
        data: Map<String, String>
    ) {
        serviceScope.launch {
            try {
                val notification = Notification(
                    id = UUID.randomUUID().toString(),
                    title = title,
                    message = message,
                    timestamp = System.currentTimeMillis(),
                    isRead = false,
                    data = if (data.isNotEmpty()) data else null
                )
                val result = NotificationsModule.saveNotificationUseCase(notification)
                result.onSuccess {
                    println("✅ Notificación guardada localmente: $title")
                }.onFailure { error ->
                    println("❌ Error al guardar notificación: ${error.message}")
                }
            } catch (e: Exception) {
                println("❌ Error al guardar notificación localmente: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}
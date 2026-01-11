package com.danimed.agent_app.shared.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager as AndroidNotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessaging

actual class NotificationManager(private val context: Context) {
    
    private val notificationManager = NotificationManagerCompat.from(context)
    
    init {
        createNotificationChannels()
    }
    
    actual fun showLocalNotification(
        title: String,
        message: String,
        channelId: String
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
            ) {
                println("⚠️ No hay permisos para mostrar notificaciones")
                return
            }
        }
        
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
        println("✅ Notificación local mostrada: $title")
    }
    
    actual fun initializePushNotifications(onTokenReceived: (String) -> Unit) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                println("🔑 FCM Token obtenido: $token")
                onTokenReceived(token)
            } else {
                println("❌ Error obteniendo token: ${task.exception?.message}")
            }
        }
    }
    
    actual fun requestNotificationPermission() {
        println("ℹ️ Los permisos se solicitan desde MainActivity")
    }
    
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channels = listOf(
                NotificationChannel(
                    "default",
                    "Notificaciones Generales",
                    AndroidNotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Notificaciones generales de la aplicación"
                },
                NotificationChannel(
                    "local_events",
                    "Eventos Locales",
                    AndroidNotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Notificaciones de eventos locales"
                },
                NotificationChannel(
                    "backend_updates",
                    "Actualizaciones del Servidor",
                    AndroidNotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Notificaciones desde el backend"
                }
            )
            
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) 
                as AndroidNotificationManager
            channels.forEach { 
                manager.createNotificationChannel(it)
                println("📢 Canal creado: ${it.id}")
            }
        }
    }
}
package com.danimed.agent_app.shared.notifications

expect class NotificationManager {
    fun showLocalNotification(
        title: String,
        message: String,
        channelId: String = "default"
    )
    
    fun initializePushNotifications(onTokenReceived: (String) -> Unit)
    fun requestNotificationPermission()
}
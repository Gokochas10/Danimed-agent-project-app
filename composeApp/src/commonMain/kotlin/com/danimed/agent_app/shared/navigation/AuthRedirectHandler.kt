package com.danimed.agent_app.shared.navigation

object AuthRedirectHandler {
    private var onUnauthorizedCallback: (() -> Unit)? = null
    
    fun setOnUnauthorizedCallback(callback: () -> Unit) {
        onUnauthorizedCallback = callback
    }
    
    fun clearCallback() {
        onUnauthorizedCallback = null
    }
    
    fun notifyUnauthorized() {
        onUnauthorizedCallback?.invoke()
    }
}








package com.danimed.agent_app.shared.navigation

object AuthRedirectHandler {
    private var onUnauthorizedCallback: (() -> Unit)? = null
    private var onAutoReloginCallback: ((String) -> Unit)? = null
    
    fun setOnUnauthorizedCallback(callback: () -> Unit) {
        onUnauthorizedCallback = callback
    }
    
    fun setOnAutoReloginCallback(callback: (String) -> Unit) {
        onAutoReloginCallback = callback
    }
    
    fun clearCallback() {
        onUnauthorizedCallback = null
        onAutoReloginCallback = null
    }
    
    fun notifyUnauthorized() {
        onUnauthorizedCallback?.invoke()
    }
    
    fun attemptAutoRelogin(newToken: String) {
        onAutoReloginCallback?.invoke(newToken)
    }
}













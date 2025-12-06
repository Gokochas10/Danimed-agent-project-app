package com.danimed.agent_app.shared.utils

object SplashState {
    private var hasShownSplash = false
    private var hasCheckedInitialAuth = false
    
    fun markSplashShown() {
        hasShownSplash = true
    }
    
    fun hasShownSplash(): Boolean = hasShownSplash
    
    fun markInitialAuthChecked() {
        hasCheckedInitialAuth = true
    }
    
    fun hasCheckedInitialAuth(): Boolean = hasCheckedInitialAuth
}


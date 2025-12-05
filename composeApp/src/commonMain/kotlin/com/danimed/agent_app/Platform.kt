package com.danimed.agent_app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
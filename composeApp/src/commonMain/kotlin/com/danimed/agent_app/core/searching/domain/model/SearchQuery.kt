package com.danimed.agent_app.core.searching.domain.model

data class SearchQuery(
    val query: String,
    val timestamp: Long = System.currentTimeMillis()
)



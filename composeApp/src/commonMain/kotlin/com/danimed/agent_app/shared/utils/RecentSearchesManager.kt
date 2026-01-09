package com.danimed.agent_app.shared.utils

expect object RecentSearchesManagerProvider {
    fun getRecentSearchesManager(): RecentSearchesManager
}

interface RecentSearchesManager {
    fun saveSearch(query: String)
    fun getRecentSearches(): List<String>
    fun clearSearches()
}



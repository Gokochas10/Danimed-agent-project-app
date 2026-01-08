package com.danimed.agent_app.shared.utils

import platform.Foundation.NSUserDefaults
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString

class RecentSearchesManagerImpl : RecentSearchesManager {
    private val userDefaults = NSUserDefaults.standardUserDefaults
    private val key = "recent_searches"
    private val maxSearches = 10
    private val json = Json { ignoreUnknownKeys = true }

    override fun saveSearch(query: String) {
        val searches = getRecentSearches().toMutableList()
        // Remove if already exists to avoid duplicates
        searches.remove(query)
        // Add to the beginning
        searches.add(0, query)
        // Keep only the last maxSearches
        val trimmed = searches.take(maxSearches)
        val jsonString = json.encodeToString(trimmed)
        userDefaults.setObject(jsonString, key)
    }

    override fun getRecentSearches(): List<String> {
        val jsonString = userDefaults.objectForKey(key) as? String ?: return emptyList()
        return try {
            json.decodeFromString<List<String>>(jsonString)
        } catch (e: Exception) {
            emptyList()
        }
    }

    override fun clearSearches() {
        userDefaults.removeObjectForKey(key)
    }
}

actual object RecentSearchesManagerProvider {
    actual fun getRecentSearchesManager(): RecentSearchesManager {
        return RecentSearchesManagerImpl()
    }
}


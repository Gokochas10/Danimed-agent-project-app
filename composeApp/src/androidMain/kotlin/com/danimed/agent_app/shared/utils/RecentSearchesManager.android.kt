package com.danimed.agent_app.shared.utils

import android.content.Context
import android.content.SharedPreferences
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString

class RecentSearchesManagerImpl(private val context: Context) : RecentSearchesManager {
    private val prefs: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val json = Json { ignoreUnknownKeys = true }
    private val key = "recent_searches"
    private val maxSearches = 10

    override fun saveSearch(query: String) {
        val searches = getRecentSearches().toMutableList()
        // Remove if already exists to avoid duplicates
        searches.remove(query)
        // Add to the beginning
        searches.add(0, query)
        // Keep only the last maxSearches
        val trimmed = searches.take(maxSearches)
        val jsonString = json.encodeToString(trimmed)
        prefs.edit().putString(key, jsonString).apply()
    }

    override fun getRecentSearches(): List<String> {
        val jsonString = prefs.getString(key, null) ?: return emptyList()
        return try {
            json.decodeFromString<List<String>>(jsonString)
        } catch (e: Exception) {
            emptyList()
        }
    }

    override fun clearSearches() {
        prefs.edit().remove(key).apply()
    }
}

actual object RecentSearchesManagerProvider {
    private var context: Context? = null

    fun init(context: Context) {
        RecentSearchesManagerProvider.context = context
    }

    actual fun getRecentSearchesManager(): RecentSearchesManager {
        return RecentSearchesManagerImpl(context ?: throw IllegalStateException("RecentSearchesManagerProvider not initialized"))
    }
}


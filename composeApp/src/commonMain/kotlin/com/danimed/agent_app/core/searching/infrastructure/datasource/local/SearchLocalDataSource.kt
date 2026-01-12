package com.danimed.agent_app.core.searching.infrastructure.datasource.local

import com.danimed.agent_app.core.searching.domain.model.SearchQuery
import com.danimed.agent_app.shared.utils.RecentSearchesManagerProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchLocalDataSource {
    private val manager = RecentSearchesManagerProvider.getRecentSearchesManager()
    
    suspend fun saveRecentSearch(query: String): Result<Unit> {
        return try {
            withContext(Dispatchers.Default) {
                manager.saveSearch(query)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getRecentSearches(): Result<List<SearchQuery>> {
        return try {
            val searches = withContext(Dispatchers.Default) {
                manager.getRecentSearches()
            }
            // Create SearchQuery objects with timestamps
            val queries = searches.mapIndexed { index, query ->
                SearchQuery(query, System.currentTimeMillis() - (index * 1000L))
            }
            Result.success(queries)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun clearRecentSearches(): Result<Unit> {
        return try {
            withContext(Dispatchers.Default) {
                manager.clearSearches()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}


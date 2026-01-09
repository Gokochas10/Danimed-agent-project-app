package com.danimed.agent_app.core.searching.domain.repository

import com.danimed.agent_app.core.searching.domain.model.SearchQuery

interface SearchRepository {
    suspend fun saveRecentSearch(query: String): Result<Unit>
    suspend fun getRecentSearches(): Result<List<SearchQuery>>
    suspend fun clearRecentSearches(): Result<Unit>
}



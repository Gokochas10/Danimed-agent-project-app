package com.danimed.agent_app.core.searching.infrastructure.repository

import com.danimed.agent_app.core.searching.domain.model.SearchQuery
import com.danimed.agent_app.core.searching.domain.repository.SearchRepository
import com.danimed.agent_app.core.searching.infrastructure.datasource.local.SearchLocalDataSource

class SearchRepositoryImpl(
    private val localDataSource: SearchLocalDataSource
) : SearchRepository {
    override suspend fun saveRecentSearch(query: String): Result<Unit> {
        return localDataSource.saveRecentSearch(query)
    }

    override suspend fun getRecentSearches(): Result<List<SearchQuery>> {
        return localDataSource.getRecentSearches()
    }

    override suspend fun clearRecentSearches(): Result<Unit> {
        return localDataSource.clearRecentSearches()
    }
}


package com.danimed.agent_app.core.searching.domain.usecase

import com.danimed.agent_app.core.searching.domain.model.SearchQuery
import com.danimed.agent_app.core.searching.domain.repository.SearchRepository

class GetRecentSearchesUseCase(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(): Result<List<SearchQuery>> {
        return repository.getRecentSearches()
    }
}


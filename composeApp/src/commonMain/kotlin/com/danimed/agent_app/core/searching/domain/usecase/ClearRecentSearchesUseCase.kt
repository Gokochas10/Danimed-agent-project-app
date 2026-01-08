package com.danimed.agent_app.core.searching.domain.usecase

import com.danimed.agent_app.core.searching.domain.repository.SearchRepository

class ClearRecentSearchesUseCase(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return repository.clearRecentSearches()
    }
}


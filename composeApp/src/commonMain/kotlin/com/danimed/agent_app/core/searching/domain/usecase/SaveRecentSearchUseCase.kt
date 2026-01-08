package com.danimed.agent_app.core.searching.domain.usecase

import com.danimed.agent_app.core.searching.domain.repository.SearchRepository

class SaveRecentSearchUseCase(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(query: String): Result<Unit> {
        if (query.isBlank()) {
            return Result.failure(IllegalArgumentException("Query cannot be empty"))
        }
        return repository.saveRecentSearch(query.trim())
    }
}


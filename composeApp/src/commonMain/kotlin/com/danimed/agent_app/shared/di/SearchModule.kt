package com.danimed.agent_app.shared.di

import com.danimed.agent_app.core.searching.domain.repository.SearchRepository
import com.danimed.agent_app.core.searching.domain.usecase.ClearRecentSearchesUseCase
import com.danimed.agent_app.core.searching.domain.usecase.GetRecentSearchesUseCase
import com.danimed.agent_app.core.searching.domain.usecase.SaveRecentSearchUseCase
import com.danimed.agent_app.core.searching.infrastructure.datasource.local.SearchLocalDataSource
import com.danimed.agent_app.core.searching.infrastructure.repository.SearchRepositoryImpl

object SearchModule {
    private val localDataSource = SearchLocalDataSource()
    val searchRepository: SearchRepository = SearchRepositoryImpl(localDataSource)
    val getRecentSearchesUseCase = GetRecentSearchesUseCase(searchRepository)
    val saveRecentSearchUseCase = SaveRecentSearchUseCase(searchRepository)
    val clearRecentSearchesUseCase = ClearRecentSearchesUseCase(searchRepository)
}



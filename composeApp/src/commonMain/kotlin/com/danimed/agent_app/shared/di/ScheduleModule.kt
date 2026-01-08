package com.danimed.agent_app.shared.di

import com.danimed.agent_app.core.scheduling.presentation.domain.repository.ScheduleRepository
import com.danimed.agent_app.core.scheduling.presentation.domain.usecase.GetSchedulesUseCase
import com.danimed.agent_app.core.scheduling.presentation.infrastructure.api.ScheduleApi
import com.danimed.agent_app.core.scheduling.presentation.infrastructure.datasource.remote.ScheduleRemoteDataSource
import com.danimed.agent_app.core.scheduling.presentation.infrastructure.repository.ScheduleRepositoryImpl
import com.danimed.agent_app.shared.networks.createHttpClient

object ScheduleModule {
    private val httpClient = createHttpClient()
    private val scheduleApi =
        _root_ide_package_.com.danimed.agent_app.core.scheduling.presentation.infrastructure.api.ScheduleApi(httpClient)
    private val scheduleRemoteDataSource =
        _root_ide_package_.com.danimed.agent_app.core.scheduling.presentation.infrastructure.datasource.remote.ScheduleRemoteDataSource(
            scheduleApi
        )
    val scheduleRepository: com.danimed.agent_app.core.scheduling.presentation.domain.repository.ScheduleRepository =
        _root_ide_package_.com.danimed.agent_app.core.scheduling.presentation.infrastructure.repository.ScheduleRepositoryImpl(
            scheduleRemoteDataSource
        )
    val getSchedulesUseCase =
        _root_ide_package_.com.danimed.agent_app.core.scheduling.presentation.domain.usecase.GetSchedulesUseCase(
            scheduleRepository
        )
}





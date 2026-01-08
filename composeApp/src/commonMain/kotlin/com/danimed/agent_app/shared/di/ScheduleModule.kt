package com.danimed.agent_app.shared.di

import com.danimed.agent_app.core.schedule.domain.repository.ScheduleRepository
import com.danimed.agent_app.core.schedule.domain.usecase.GetSchedulesUseCase
import com.danimed.agent_app.core.schedule.infrastructure.api.ScheduleApi
import com.danimed.agent_app.core.schedule.infrastructure.datasource.remote.ScheduleRemoteDataSource
import com.danimed.agent_app.core.schedule.infrastructure.repository.ScheduleRepositoryImpl
import com.danimed.agent_app.shared.networks.createHttpClient

object ScheduleModule {
    private val httpClient = createHttpClient()
    private val scheduleApi = ScheduleApi(httpClient)
    private val scheduleRemoteDataSource = ScheduleRemoteDataSource(scheduleApi)
    val scheduleRepository: ScheduleRepository = ScheduleRepositoryImpl(scheduleRemoteDataSource)
    val getSchedulesUseCase = GetSchedulesUseCase(scheduleRepository)
}





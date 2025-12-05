package com.danimed.agent_app.shared.networks.dto

import kotlinx.serialization.Serializable

@Serializable
data class ApiPaginatedRes<T>(
    val records: List<T>,
    val total: Int,
    val limit: Int,
    val page: Int,
    val pages: Int
)


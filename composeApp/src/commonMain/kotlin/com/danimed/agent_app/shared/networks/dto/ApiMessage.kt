package com.danimed.agent_app.shared.networks.dto

import kotlinx.serialization.Serializable

@Serializable
data class ApiMessage(
    val content: List<String>,
    val displayable: Boolean
)



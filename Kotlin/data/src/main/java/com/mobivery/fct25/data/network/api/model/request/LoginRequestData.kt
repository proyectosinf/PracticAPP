package com.mobivery.fct25.data.network.api.model.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestData (
    val user: String,
    val password: String
)
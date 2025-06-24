package com.mobivery.fct25.data.network.api.model.response

import kotlinx.serialization.Serializable
import com.mobivery.template.domain.model.auth.RegisterResult

@Serializable
data class RegisterResponseData(
    val success: Boolean? = null,
    val message: String? = null,
    val accessToken: String? = null,
    val refreshToken: String? = null
) {
    companion object {
        fun mock() = RegisterResponseData(success = true, message = "User registered successfully")
    }
}

fun RegisterResponseData.toDomain() = RegisterResult(
    success = success == true,
    message = message,
    accessToken = accessToken,
    refreshToken = refreshToken
)

package com.mobivery.fct25.data.network.api.model.response

import com.mobivery.fct25.domain.model.auth.UserCredentials
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseData(
    val accessToken: String?,
    val refreshToken: String?,
) {
    companion object {
        fun mock() = LoginResponseData("token", "refreshToken")
    }
}

fun LoginResponseData.toDomain() = UserCredentials(accessToken = accessToken, refreshToken = refreshToken)

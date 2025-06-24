package com.mobivery.fct25.domain.model.auth

data class UserCredentials(
    val accessToken: String?,
    val refreshToken: String?,
) {
    companion object {
        fun mock() = UserCredentials("token", "refreshToken")
    }
}

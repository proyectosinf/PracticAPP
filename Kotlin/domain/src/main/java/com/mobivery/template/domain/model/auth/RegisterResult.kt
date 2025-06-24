package com.mobivery.template.domain.model.auth

data class RegisterResult(
    val success: Boolean,
    val message: String? = null,
    val accessToken: String? = null,
    val refreshToken: String? = null
)

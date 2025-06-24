package com.mobivery.template.domain.model.auth

data class UserSession(
    val email: String,
    val firebaseUid: String,
    val role: Int,
    val companyId: Int?
)

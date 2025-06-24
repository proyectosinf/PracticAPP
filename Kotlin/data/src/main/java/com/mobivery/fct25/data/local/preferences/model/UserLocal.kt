package com.mobivery.fct25.data.local.preferences.model

import kotlinx.serialization.Serializable

@Serializable
data class UserLocal(
    val email: String,
    val firebaseUid: String,
    val role: Int,
    val companyId: Int? = null
)

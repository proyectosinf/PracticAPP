package com.mobivery.template.domain.model.user

data class User(
    val id: Int,
    val firebaseUid: String,
    val name: String,
    val surname: String,
    val email: String,
    val dni: String? = null,
    val socialSecurityNumber: String? = null,
    val pdfCV: String? = null,
    val contactName: String? = null,
    val contactEmail: String? = null,
    val contactPhone: String? = null,
    val role: UserRole,
    val companyId: Int? = null,
    val photo: String? = null,
)

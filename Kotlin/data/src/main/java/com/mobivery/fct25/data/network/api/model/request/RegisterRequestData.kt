package com.mobivery.fct25.data.network.api.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequestData(
    // Common fields for both user types
    val email: String,
    val name: String,
    val surname: String,
    val role: Int,
    val photo: String? = null,
    @SerialName("uid")
    val firebaseUid: String,

    // Student fields
    val dni: String? = null,
    @SerialName("social_security_number")
    val socialSecurityNumber: String? = null,
    @SerialName("pdf_cv")
    val pdfCv: String? = null,
    @SerialName("contact_name")
    val contactName: String? = null,
    @SerialName("contact_email")
    val contactEmail: String? = null,
    @SerialName("contact_phone")
    val contactPhone: String? = null,

    // Work tutor field
    @SerialName("company_id")
    val companyId: Int? = null,

    // Optional field
    @SerialName("security_code")
    val securityCode: String? = null
)

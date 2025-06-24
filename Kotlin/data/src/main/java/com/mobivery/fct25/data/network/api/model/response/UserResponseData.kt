package com.mobivery.fct25.data.network.api.model.response

import com.mobivery.template.domain.model.user.User
import com.mobivery.template.domain.model.user.UserRole
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponseData(
    val id: Int,
    val uid: String,
    val name: String,
    val surname: String,
    val email: String,
    val dni: String? = null,
    @SerialName("social_security_number")
    val socialSecurityNumber: String? = null,
    @SerialName("pdf_cv")
    val pdfCV: String? = null,
    @SerialName("contact_name")
    val contactName: String? = null,
    @SerialName("contact_email")
    val contactEmail: String? = null,
    @SerialName("contact_phone")
    val contactPhone: String? = null,
    @SerialName("company_id")
    val companyId: Int? = null,
    val role: Int
)

fun UserResponseData.toDomain() = User(
    id = id,
    firebaseUid = uid,
    name = name,
    surname = surname,
    email = email,
    dni = dni,
    socialSecurityNumber = socialSecurityNumber,
    pdfCV = pdfCV,
    contactName = contactName,
    contactEmail = contactEmail,
    contactPhone = contactPhone,
    companyId = companyId,
    role = UserRole.from(role)
)

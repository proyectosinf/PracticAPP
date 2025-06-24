package com.mobivery.fct25.data.network.api.model.response

import com.mobivery.template.domain.model.candidacy.CandidacyDetail
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class CandidacyDetailResponseData(
    @SerialName("id")
    val id: Int,

    @SerialName("status")
    val status: Int,

    @SerialName("postulation_date")
    val postulationDate: String,

    @SerialName("additional_notes")
    val additionalNotes: String?,

    @SerialName("presentation_card")
    val presentationCard: String?,

    @SerialName("offer_title")
    val offerTitle: String,

    @SerialName("student_name")
    val studentName: String,

    @SerialName("student_surname")
    val studentSurname: String,

    @SerialName("student_email")
    val studentEmail: String,

    @SerialName("contact_phone")
    val contactPhone: String,

    @SerialName("student_photo")
    val studentPhoto: String?,

    @SerialName("company_name")
    val companyName: String,

    @SerialName("contact_email")
    val companyEmail: String,

    @SerialName("contact_name")
    val contactName: String?,

    @SerialName("company_photo")
    val companyPhoto: String?
)

fun CandidacyDetailResponseData.toDomainModel(): CandidacyDetail =
    CandidacyDetail(
        id = id,
        status = status.intStatusToDomain(),
        postulationDate = LocalDate.parse(postulationDate),
        additionalNotes = additionalNotes,
        presentationCard = presentationCard,
        offerTitle = offerTitle,
        studentName = studentName,
        studentSurname = studentSurname,
        studentEmail = studentEmail,
        contactPhone = contactPhone,
        studentPhoto = studentPhoto,
        companyName = companyName,
        companyEmail = companyEmail,
        contactName = contactName,
        companyPhoto = companyPhoto,
    )

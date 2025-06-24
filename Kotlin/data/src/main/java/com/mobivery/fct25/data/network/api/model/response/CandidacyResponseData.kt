package com.mobivery.fct25.data.network.api.model.response

import com.mobivery.template.domain.model.candidacy.Candidacy
import com.mobivery.template.domain.model.candidacy.CandidacyStatus
import com.mobivery.template.domain.model.candidacy.CandidacyStatus.ACCEPTED
import com.mobivery.template.domain.model.candidacy.CandidacyStatus.PENDING
import com.mobivery.template.domain.model.candidacy.CandidacyStatus.REJECTED
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class CandidacyResponseData(
    val id: Int,
    val status: Int,

    @SerialName("postulation_date")
    val postulationDate: String,

    @SerialName("student_name")
    val studentName: String,

    @SerialName("student_surname")
    val studentSurname: String,

    @SerialName("offer_title")
    val offerTitle: String,

    @SerialName("company_name")
    val companyName: String
)

fun CandidacyResponseData.toDomainModel(): Candidacy {
    return Candidacy(
        id = id,
        status = status.intStatusToDomain(),
        postulationDate = LocalDate.parse(postulationDate),
        studentName = studentName,
        studentSurname = studentSurname,
        offerTitle = offerTitle,
        companyName = companyName
    )
}

fun Int.intStatusToDomain(): CandidacyStatus = when (this) {
    1 -> PENDING
    2 -> ACCEPTED
    3 -> REJECTED
    else -> PENDING
}
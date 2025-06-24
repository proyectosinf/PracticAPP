package com.mobivery.template.domain.model.candidacy

import java.time.LocalDate

data class CandidacyDetail(
    val id: Int,
    val status: CandidacyStatus,
    val postulationDate: LocalDate,
    val additionalNotes: String?,
    val presentationCard: String?,
    val offerTitle: String,
    val studentName: String,
    val studentSurname: String,
    val studentEmail: String,
    val studentPhoto: String?,
    val companyName: String,
    val companyEmail: String,
    val contactPhone: String?,
    val companyPhoto: String?,
    val contactName: String?
)

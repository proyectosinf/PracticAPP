package com.mobivery.template.domain.model.candidacy

import java.time.LocalDate

data class Candidacy(
    val id: Int,
    val status: CandidacyStatus,
    val postulationDate: LocalDate,
    val studentName: String,
    val studentSurname: String,
    val offerTitle: String,
    val companyName: String
)

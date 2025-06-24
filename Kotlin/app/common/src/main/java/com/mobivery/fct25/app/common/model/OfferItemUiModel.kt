package com.mobivery.fct25.app.common.model

import java.time.LocalDate

data class OfferItemUiModel(
    val id: Int,
    val title: String,
    val degree: String,
    val vacancies: Int,
    val views: Int = 0,
    val company: String? = null,
    val logoUrl: String? = null,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val isEnrolled: Boolean = false,
    val status: OfferStatus? = null,
    val inscriptionsCandidacy: Int,
    val typeLabel: String
)

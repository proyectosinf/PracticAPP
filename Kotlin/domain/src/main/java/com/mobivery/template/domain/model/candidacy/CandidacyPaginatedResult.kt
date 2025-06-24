package com.mobivery.template.domain.model.candidacy

data class CandidaciesPaginatedResult(
    val candidacies: List<Candidacy>,
    val total: Int
)
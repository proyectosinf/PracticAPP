package com.mobivery.template.domain.repository

import com.mobivery.template.domain.model.candidacy.CandidaciesPaginatedResult
import com.mobivery.template.domain.model.candidacy.CandidacyDetail
import com.mobivery.template.domain.model.candidacy.CandidacyStatus

interface CandidacyRepository {
    suspend fun getCandidaciesByOfferId(
        offerId: Int,
        page: Int,
        limit: Int
    ): CandidaciesPaginatedResult

    suspend fun getUserCandidacies(
        page: Int,
        limit: Int
    ): CandidaciesPaginatedResult

    suspend fun getCandidacyById(id: Int): CandidacyDetail

    suspend fun updateCandidacyState(
        id: Int,
        status: CandidacyStatus,
        additionalNotes: String?
    ): CandidacyDetail
}

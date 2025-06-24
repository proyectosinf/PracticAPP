package com.mobivery.fct25.data.repository

import com.mobivery.fct25.data.network.api.CandidacyApi
import com.mobivery.fct25.data.network.api.model.request.UpdateCandidacyStateRequestData
import com.mobivery.fct25.data.network.api.model.response.toDomainModel
import com.mobivery.template.domain.model.candidacy.CandidaciesPaginatedResult
import com.mobivery.template.domain.model.candidacy.CandidacyDetail
import com.mobivery.template.domain.model.candidacy.CandidacyStatus
import com.mobivery.template.domain.repository.CandidacyRepository
import javax.inject.Inject

class CandidacyRepositoryImpl @Inject constructor(
    private val candidacyApi: CandidacyApi
) : CandidacyRepository {

    override suspend fun getCandidaciesByOfferId(
        offerId: Int,
        page: Int,
        limit: Int
    ): CandidaciesPaginatedResult =
        candidacyApi.getCandidaciesByOfferId(
            offerId = offerId,
            page = page,
            limit = limit
        ).let {
            CandidaciesPaginatedResult(
                candidacies = it.items.map { item -> item.toDomainModel() },
                total = it.total
            )
        }

    override suspend fun getUserCandidacies(
        page: Int,
        limit: Int
    ): CandidaciesPaginatedResult =
        candidacyApi.getMyCandidacies(
            page = page,
            limit = limit
        ).let {
            CandidaciesPaginatedResult(
                candidacies = it.items.map { item -> item.toDomainModel() },
                total = it.total
            )
        }

    override suspend fun getCandidacyById(id: Int): CandidacyDetail =
        candidacyApi.getCandidacyById(id).toDomainModel()

    override suspend fun updateCandidacyState(
        id: Int,
        status: CandidacyStatus,
        additionalNotes: String?
    ): CandidacyDetail =
        candidacyApi.updateCandidacyState(
            id,
            UpdateCandidacyStateRequestData(
                status = status.value,
                additionalNotes = additionalNotes
            )
        ).toDomainModel()
}

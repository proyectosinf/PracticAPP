package com.mobivery.fct25.data.repository

import Offer
import com.mobivery.fct25.data.datasources.APIDataSource
import com.mobivery.fct25.data.network.api.model.request.CreateOfferRequestData.Companion.toRequestData
import com.mobivery.fct25.data.network.api.model.request.RegisterCandidacyRequestData
import com.mobivery.fct25.data.network.api.model.response.toDomain
import com.mobivery.fct25.domain.repository.AuthRepository
import com.mobivery.template.domain.model.degree.Degree
import com.mobivery.template.domain.model.offer.OffersPaginated
import com.mobivery.template.domain.repository.OfferRepository
import javax.inject.Inject

class OfferRepositoryImpl @Inject constructor(
    private val apiDataSource: APIDataSource,
    private val authRepository: AuthRepository
) : OfferRepository {

    override suspend fun getOffers(page: Int, limit: Int): OffersPaginated {
        authRepository.refreshTokenIfNeeded()
        val response = apiDataSource.getPaginatedOffers(page, limit)
        return OffersPaginated(
            offers = response.items.map { it.toDomain() },
            total = response.total
        )
    }

    override suspend fun getDegrees(): List<Degree> {
        authRepository.refreshTokenIfNeeded()
        return apiDataSource.getDegrees().map { it.toDomain() }
    }

    override suspend fun createOffer(offer: Offer): Offer {
        authRepository.refreshTokenIfNeeded()
        val response = apiDataSource.createOffer(offer.toRequestData())
        return response.toDomain()
    }

    override suspend fun getOfferById(offerId: Int): Offer {
        authRepository.refreshTokenIfNeeded()
        val response = apiDataSource.getOfferById(offerId)
        return response.toDomain()
    }

    override suspend fun registerCandidacy(presentationCard: String, offerId: Int) {
        authRepository.refreshTokenIfNeeded()
        apiDataSource.registerCandidacy(
            RegisterCandidacyRequestData(
                presentationCard = presentationCard,
                offerId = offerId
            )
        )
    }
}

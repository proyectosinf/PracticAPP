package com.mobivery.template.domain.repository

import Offer
import com.mobivery.template.domain.model.degree.Degree
import com.mobivery.template.domain.model.offer.OffersPaginated

interface OfferRepository {
    suspend fun getOffers(page: Int, limit: Int): OffersPaginated
    suspend fun createOffer(offer: Offer): Offer
    suspend fun getDegrees(): List<Degree>
    suspend fun getOfferById(offerId: Int): Offer
    suspend fun registerCandidacy(presentationCard: String, offerId: Int)
}

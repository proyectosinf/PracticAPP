package com.mobivery.template.domain.model.offer

import Offer

data class OffersPaginated(
    val offers: List<Offer>,
    val total: Int
)

package com.mobivery.fct25.data.network.api.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterCandidacyRequestData(
    @SerialName("presentation_card")
    val presentationCard: String,
    @SerialName("offer_id")
    val offerId: Int
)
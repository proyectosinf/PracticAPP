package com.mobivery.fct25.data.network.api.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OffersPaginatedResponseData(
    @SerialName("items")
    val items: List<OfferResponseData>,
    @SerialName("total")
    val total: Int
)

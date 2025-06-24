package com.mobivery.fct25.data.network.api.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CandidaciesPaginatedResultResponseData(

    @SerialName("items")
    val items: List<CandidacyResponseData>,

    @SerialName("total")
    val total: Int
)

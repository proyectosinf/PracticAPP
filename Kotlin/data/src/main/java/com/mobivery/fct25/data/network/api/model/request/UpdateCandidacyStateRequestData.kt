package com.mobivery.fct25.data.network.api.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateCandidacyStateRequestData(
    @SerialName("status")
    val status: Int,

    @SerialName("additional_notes")
    val additionalNotes: String?
)

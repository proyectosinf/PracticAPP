package com.mobivery.fct25.data.network.api.model.request

import Offer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateOfferRequestData(
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String = "",
    @SerialName("vacancies_number")
    val vacancies: Int,
    @SerialName("start_date")
    val startDate: String,
    @SerialName("end_date")
    val endDate: String? = null,
    @SerialName("type")
    val type: Int,
    @SerialName("degree_id")
    val degreeId: String,
    @SerialName("address")
    val address: String,
    @SerialName("postal_code")
    val postalCode: String,
    @SerialName("contact_name")
    val contactName: String,
    @SerialName("contact_email")
    val contactEmail: String,
    @SerialName("contact_phone")
    val contactPhone: String? = null
) {
    companion object {
        fun Offer.toRequestData(): CreateOfferRequestData = CreateOfferRequestData(
            title = title,
            description = description ?: "",
            vacancies = vacancies,
            startDate = startDate.toString(),
            endDate = endDate?.toString(),
            type = type,
            degreeId = degreeId,
            address = address,
            postalCode = postalCode,
            contactName = contactName,
            contactEmail = contactEmail,
            contactPhone = contactPhone
        )
    }
}

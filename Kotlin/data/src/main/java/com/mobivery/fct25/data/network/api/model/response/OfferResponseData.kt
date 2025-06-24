package com.mobivery.fct25.data.network.api.model.response

import Offer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class OfferResponseData(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("vacancies_number")
    val vacancies: Int,
    @SerialName("start_date")
    val startDate: String,
    @SerialName("end_date")
    val endDate: String,
    @SerialName("views")
    val views: Int,
    @SerialName("type")
    val type: Int,
    @SerialName("address")
    val address: String,
    @SerialName("postal_code")
    val postalCode: String,
    @SerialName("contact_name")
    val contactName: String,
    @SerialName("contact_email")
    val contactEmail: String,
    @SerialName("contact_phone")
    val contactPhone: String? = null,
    @SerialName("company")
    val company: String,
    @SerialName("degree_id")
    val degreeId: String? = null,
    @SerialName("degree")
    val degreeName: String,
    @SerialName("company_photo")
    val companyPhoto: String? = null,
    @SerialName("inscribe")
    val inscribe: Boolean? = null,
    @SerialName("presentation_card")
    val presentationCard: String? = null,
    @SerialName("inscriptions_candidacy")
    val inscriptionsCandidacy: Int = 0,
)

fun OfferResponseData.toDomain() = Offer(
    id = id,
    title = title,
    description = description,
    vacancies = vacancies,
    startDate = LocalDate.parse(startDate),
    endDate = LocalDate.parse(endDate),
    views = views,
    type = type,
    address = address,
    postalCode = postalCode,
    contactName = contactName,
    contactEmail = contactEmail,
    contactPhone = contactPhone ?: "",
    company = company,
    degreeId = degreeId ?: "",
    degreeName = degreeName,
    companyPhoto = companyPhoto ?: "",
    inscribe = inscribe,
    presentationCard = presentationCard,
    inscriptionsCandidacy = inscriptionsCandidacy,
)

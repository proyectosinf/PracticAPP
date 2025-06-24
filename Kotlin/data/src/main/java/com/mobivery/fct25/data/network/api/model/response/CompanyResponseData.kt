package com.mobivery.fct25.data.network.api.model.response

import com.mobivery.template.domain.model.company.CompanyModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CompanyResponseData(
    val id: Int,
    val name: String,
    val sector: String,
    val web: String? = null,
    val cif: String,
    val logo: String? = null,
    @SerialName("security_code")
    val securityCode: String? = null
)

fun CompanyResponseData.toDomain(): CompanyModel {
    return CompanyModel(
        name = name,
        cif = cif,
        sector = sector,
        web = web,
        logo = logo,
        securityCode = securityCode
    )
}

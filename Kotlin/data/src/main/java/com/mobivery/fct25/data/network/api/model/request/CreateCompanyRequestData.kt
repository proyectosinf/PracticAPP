package com.mobivery.fct25.data.network.api.model.request

import com.mobivery.template.domain.model.company.CompanyModel
import kotlinx.serialization.Serializable

@Serializable
data class CreateCompanyRequestData(
    val name: String,
    val cif: String,
    val sector: String,
    val web: String? = null
) {
    companion object {
        fun CompanyModel.toRequestData(): CreateCompanyRequestData = CreateCompanyRequestData(
            name = name,
            cif = cif,
            sector = sector,
            web = web
        )
    }
}

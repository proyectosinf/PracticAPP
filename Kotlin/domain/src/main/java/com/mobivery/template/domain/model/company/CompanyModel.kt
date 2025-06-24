package com.mobivery.template.domain.model.company

data class CompanyModel(
    val name: String = "",
    val cif: String = "",
    val sector: String = "",
    val web: String? = null,
    val logo: String? = null,
    val securityCode: String? = null,
)

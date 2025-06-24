package com.mobivery.template.domain.repository

import android.net.Uri
import com.mobivery.template.domain.model.company.CompanyModel

interface CompanyRepository {
    suspend fun createCompany(company: CompanyModel)
    suspend fun getCurrentUserCompany(): CompanyModel
    suspend fun uploadCompanyImage(uri: Uri)
}

package com.mobivery.fct25.data.network.api

import com.mobivery.fct25.data.network.api.model.request.CreateCompanyRequestData
import com.mobivery.fct25.data.network.api.model.response.CompanyResponseData
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CompanyAPI {

    @POST("companies/")
    suspend fun createCompany(
        @Body request: CreateCompanyRequestData
    ): CompanyResponseData

    @GET("companies/get_current_user_company")
    suspend fun getCurrentUserCompany(): CompanyResponseData
}

package com.mobivery.fct25.data.datasources

import com.mobivery.fct25.data.network.api.AuthAPI
import com.mobivery.fct25.data.network.api.CompanyAPI
import com.mobivery.fct25.data.network.api.OfferAPI
import com.mobivery.fct25.data.network.api.model.request.CreateCompanyRequestData
import com.mobivery.fct25.data.network.api.model.request.CreateOfferRequestData
import com.mobivery.fct25.data.network.api.model.request.LoginRequestData
import com.mobivery.fct25.data.network.api.model.request.RegisterCandidacyRequestData
import com.mobivery.fct25.data.network.api.model.request.RegisterRequestData
import com.mobivery.fct25.data.network.api.model.response.CandidacyResponseData
import com.mobivery.fct25.data.network.api.model.response.CompanyResponseData
import com.mobivery.fct25.data.network.api.model.response.DegreeResponseData
import com.mobivery.fct25.data.network.api.model.response.OfferResponseData
import com.mobivery.fct25.data.network.api.model.response.RegisterResponseData
import com.mobivery.fct25.data.network.api.model.response.UserResponseData
import javax.inject.Inject

class APIDataSource @Inject constructor(
    private val authAPI: AuthAPI,
    private val companyAPI: CompanyAPI,
    private val offerAPI: OfferAPI,
) {
    suspend fun login(userData: LoginRequestData) = authAPI.login(userData)

    suspend fun registerStudent(
        request: RegisterRequestData
    ): RegisterResponseData = authAPI.registerStudent(request)

    suspend fun registerWorkTutor(
        request: RegisterRequestData
    ): RegisterResponseData = authAPI.registerWorkTutor(request)

    suspend fun getUserByUid(): UserResponseData = authAPI.getUserByUid()

    suspend fun createCompany(
        request: CreateCompanyRequestData
    ): CompanyResponseData = companyAPI.createCompany(request)

    suspend fun getCurrentUserCompany(): CompanyResponseData =
        companyAPI.getCurrentUserCompany()

    suspend fun getPaginatedOffers(page: Int, limit: Int) =
        offerAPI.getPaginatedOffers(page, limit)

    suspend fun createOffer(request: CreateOfferRequestData): OfferResponseData =
        offerAPI.createOffer(request)

    suspend fun getDegrees(): List<DegreeResponseData> =
        offerAPI.getDegrees()

    suspend fun getOfferById(offerId: Int): OfferResponseData =
        offerAPI.getOfferById(offerId)

    suspend fun registerCandidacy(request: RegisterCandidacyRequestData): CandidacyResponseData =
        offerAPI.registerCandidacy(request)
}

package com.mobivery.fct25.data.network.api

import com.mobivery.fct25.data.network.api.model.request.CreateOfferRequestData
import com.mobivery.fct25.data.network.api.model.request.RegisterCandidacyRequestData
import com.mobivery.fct25.data.network.api.model.response.CandidacyResponseData
import com.mobivery.fct25.data.network.api.model.response.DegreeResponseData
import com.mobivery.fct25.data.network.api.model.response.OfferResponseData
import com.mobivery.fct25.data.network.api.model.response.OffersPaginatedResponseData
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface OfferAPI {

    @GET("offers/paginated")
    suspend fun getPaginatedOffers(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): OffersPaginatedResponseData

    @POST("offers/")
    suspend fun createOffer(
        @Body offerRequest: CreateOfferRequestData
    ): OfferResponseData

    @GET("degrees")
    suspend fun getDegrees(): List<DegreeResponseData>

    @GET("offers/{id}")
    suspend fun getOfferById(
        @Path("id") offerId: Int
    ): OfferResponseData

    @POST("candidacies/")
    suspend fun registerCandidacy(
        @Body request: RegisterCandidacyRequestData
    ): CandidacyResponseData
}

package com.mobivery.fct25.data.network.api

import com.mobivery.fct25.data.network.api.model.request.UpdateCandidacyStateRequestData
import com.mobivery.fct25.data.network.api.model.response.CandidaciesPaginatedResultResponseData
import com.mobivery.fct25.data.network.api.model.response.CandidacyDetailResponseData
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface CandidacyApi {

    @GET("candidacies/paginated/{offer_id}")
    suspend fun getCandidaciesByOfferId(
        @Path("offer_id") offerId: Int,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): CandidaciesPaginatedResultResponseData

    @GET("candidacies/paginated")
    suspend fun getMyCandidacies(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): CandidaciesPaginatedResultResponseData

    @GET("candidacies/by-id/{id}")
    suspend fun getCandidacyById(
        @Path("id") id: Int
    ): CandidacyDetailResponseData

    @PUT("candidacies/update-state/{id}")
    suspend fun updateCandidacyState(
        @Path("id") id: Int,
        @Body body: UpdateCandidacyStateRequestData
    ): CandidacyDetailResponseData
}

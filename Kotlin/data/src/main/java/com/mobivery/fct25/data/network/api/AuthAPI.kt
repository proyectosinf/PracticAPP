package com.mobivery.fct25.data.network.api

import com.mobivery.fct25.data.network.api.model.request.LoginRequestData
import com.mobivery.fct25.data.network.api.model.request.RefreshTokenRequestData
import com.mobivery.fct25.data.network.api.model.request.RegisterRequestData
import com.mobivery.fct25.data.network.api.model.response.LoginResponseData
import com.mobivery.fct25.data.network.api.model.response.RegisterResponseData
import com.mobivery.fct25.data.network.api.model.response.UserResponseData
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthAPI {

    @POST("login")
    suspend fun login(
        @Body request: LoginRequestData
    ): LoginResponseData

    @POST("refresh-token")
    fun refreshToken(
        @Body request: RefreshTokenRequestData
    ): LoginResponseData

    @POST("students/")
    suspend fun registerStudent(
        @Body request: RegisterRequestData
    ): RegisterResponseData

    @POST("work_tutors/")
    suspend fun registerWorkTutor(
        @Body request: RegisterRequestData
    ): RegisterResponseData

    @GET("users/get_current_user")
    suspend fun getUserByUid(): UserResponseData
}

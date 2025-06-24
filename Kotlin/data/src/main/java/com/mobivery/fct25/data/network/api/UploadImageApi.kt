package com.mobivery.fct25.data.network.api

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface UploadImageApi {

    @Multipart
    @POST("firebase/upload-image")
    suspend fun uploadImage(
        @Query("model") model: Int = 1,
        @Part file: MultipartBody.Part
    ): String
}

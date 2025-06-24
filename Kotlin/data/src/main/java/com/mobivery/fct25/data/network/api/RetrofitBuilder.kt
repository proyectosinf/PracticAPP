package com.mobivery.fct25.data.network.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mobivery.fct25.data.datasources.PreferencesDataSource
import com.mobivery.fct25.data.network.SessionProvider
import com.mobivery.fct25.data.network.constants.ACCEPT_LANGUAGE
import com.mobivery.fct25.data.network.constants.APPLICATION_JSON
import com.mobivery.fct25.data.network.constants.TIMEOUT_DURATION_SECONDS
import com.mobivery.fct25.data.network.constants.TIMEOUT_UNIT
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class RetrofitBuilder(
    private val sessionProvider: SessionProvider,
    private val preferencesDataSource: PreferencesDataSource,
    baseURL: HttpUrl,
    logEnabled: Boolean,
) {
    companion object {
        const val AUTH_HEADER_KEY = "Authorization"
        fun getAuthHeaderValue(token: String) = "Bearer $token"
    }

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
    }

    private val contentType = APPLICATION_JSON.toMediaType()

    private val logInterceptor = HttpLoggingInterceptor().apply {
        level =
            if (logEnabled) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }

    private val requestInterceptor = Interceptor { chain ->
        val original: Request = chain.request()
        val requestBuilder: Request.Builder = original.newBuilder()
        val accessToken = preferencesDataSource.getAccessToken()
        accessToken?.let {
            requestBuilder.addHeader(AUTH_HEADER_KEY, getAuthHeaderValue(it))
        }
        requestBuilder.header(ACCEPT_LANGUAGE, sessionProvider.getLanguage())
        requestBuilder.method(original.method, original.body)
        chain.proceed(requestBuilder.build())
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT_DURATION_SECONDS, TIMEOUT_UNIT)
        .readTimeout(TIMEOUT_DURATION_SECONDS, TIMEOUT_UNIT)
        .writeTimeout(TIMEOUT_DURATION_SECONDS, TIMEOUT_UNIT)
        .addInterceptor(requestInterceptor)
        .addInterceptor(logInterceptor)
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseURL)
        .client(client)
        .addConverterFactory(json.asConverterFactory(contentType))
        .build()
}

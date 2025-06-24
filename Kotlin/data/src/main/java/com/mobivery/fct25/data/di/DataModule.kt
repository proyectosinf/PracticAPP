package com.mobivery.fct25.data.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mobivery.fct25.data.BuildConfig
import com.mobivery.fct25.data.datasources.ImageUriDataSource
import com.mobivery.fct25.data.datasources.PreferencesDataSource
import com.mobivery.fct25.data.network.SessionProvider
import com.mobivery.fct25.data.network.SessionProviderImpl
import com.mobivery.fct25.data.network.api.AuthAPI
import com.mobivery.fct25.data.network.api.CandidacyApi
import com.mobivery.fct25.data.network.api.CompanyAPI
import com.mobivery.fct25.data.network.api.OfferAPI
import com.mobivery.fct25.data.network.api.RetrofitBuilder
import com.mobivery.fct25.data.network.api.UploadImageApi
import com.mobivery.fct25.data.preferences.DataStorePreferences
import com.mobivery.fct25.data.preferences.EncryptedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl.Companion.toHttpUrl
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideSessionProvider(@ApplicationContext context: Context): SessionProvider =
        SessionProviderImpl()

    @Provides
    @Singleton
    fun provideRetrofitBuilder(
        sessionProvider: SessionProvider,
        preferencesDataSource: PreferencesDataSource,
    ): RetrofitBuilder = RetrofitBuilder(
        sessionProvider = sessionProvider,
        preferencesDataSource = preferencesDataSource,
        baseURL = BuildConfig.API_URL.toHttpUrl(),
        logEnabled = BuildConfig.DEBUG
    )

    @Provides
    @Singleton
    fun provideRetrofit(retrofitBuilder: RetrofitBuilder): Retrofit =
        retrofitBuilder.retrofit

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthAPI =
        retrofit.create(AuthAPI::class.java)

    @Provides
    @Singleton
    fun provideDataStorePreferences(
        @ApplicationContext context: Context
    ): DataStorePreferences = DataStorePreferences(context)

    @Provides
    @Singleton
    fun provideEncryptedPreferences(
        @ApplicationContext context: Context
    ): EncryptedPreferences = EncryptedPreferences(context)

    @Provides
    @Singleton
    fun provideCompanyApi(retrofit: Retrofit): CompanyAPI =
        retrofit.create(CompanyAPI::class.java)

    @Provides
    @Singleton
    fun provideOfferApi(retrofit: Retrofit): OfferAPI =
        retrofit.create(OfferAPI::class.java)

    @Provides
    @Singleton
    fun provideCandidacyApi(retrofit: Retrofit): CandidacyApi =
        retrofit.create(CandidacyApi::class.java)

    @Provides
    @Singleton
    fun provideUploadImageApi(retrofit: Retrofit): UploadImageApi =
        retrofit.create(UploadImageApi::class.java)

    @Provides
    @Singleton
    fun provideImageUriDataSource(
        @ApplicationContext context: Context
    ): ImageUriDataSource = ImageUriDataSource(context)
}

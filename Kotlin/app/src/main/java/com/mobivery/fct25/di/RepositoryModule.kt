package com.mobivery.fct25.di

import com.mobivery.fct25.data.repository.AuthRepositoryImpl
import com.mobivery.fct25.data.repository.CandidacyRepositoryImpl
import com.mobivery.fct25.data.repository.CompanyRepositoryImpl
import com.mobivery.fct25.data.repository.FeatureFlagsRepositoryImpl
import com.mobivery.fct25.data.repository.OfferRepositoryImpl
import com.mobivery.fct25.data.repository.SessionRepositoryImpl
import com.mobivery.fct25.domain.repository.AuthRepository
import com.mobivery.fct25.domain.repository.FeatureFlagsRepository
import com.mobivery.fct25.domain.repository.SessionRepository
import com.mobivery.template.domain.repository.CandidacyRepository
import com.mobivery.template.domain.repository.CompanyRepository
import com.mobivery.template.domain.repository.OfferRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindFeatureFlagsRepository(
        featureFlagsRepository: FeatureFlagsRepositoryImpl
    ): FeatureFlagsRepository

    @Binds
    @Singleton
    abstract fun bindSessionRepository(
        sessionRepository: SessionRepositoryImpl
    ): SessionRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepository: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindCompanyRepository(
        companyRepository: CompanyRepositoryImpl
    ): CompanyRepository

    @Binds
    @Singleton
    abstract fun bindOfferRepository(
        offerRepositoryImpl: OfferRepositoryImpl
    ): OfferRepository

    @Binds
    @Singleton
    abstract fun bindCandidacyRepository(
        candidacyRepositoryImpl: CandidacyRepositoryImpl
    ): CandidacyRepository

}

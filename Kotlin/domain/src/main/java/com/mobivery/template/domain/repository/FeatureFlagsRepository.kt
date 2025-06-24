package com.mobivery.fct25.domain.repository

import com.mobivery.fct25.domain.model.featureflags.AppUpdate
import kotlinx.coroutines.flow.Flow

interface FeatureFlagsRepository {
    suspend fun init()
    fun showAboutTab(): Boolean
    fun showAboutTabFlow(): Flow<Boolean>
    fun updateApp(): AppUpdate?
}

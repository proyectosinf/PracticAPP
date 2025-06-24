package com.mobivery.fct25.data.repository

import com.mobivery.fct25.data.datasources.RemoteConfigDataSource
import com.mobivery.fct25.data.model.toData
import com.mobivery.fct25.data.network.SessionProvider
import com.mobivery.fct25.domain.model.featureflags.AppUpdate
import com.mobivery.fct25.domain.repository.FeatureFlagsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FeatureFlagsRepositoryImpl @Inject constructor(
    private val remoteConfigDataSource: RemoteConfigDataSource,
    private val sessionProvider: SessionProvider,
) : FeatureFlagsRepository {

    override suspend fun init() = remoteConfigDataSource.init()

    override fun showAboutTab(): Boolean {
        return featureIsActive(FeaturesEnum.ABOUT)
    }

    override fun showAboutTabFlow(): Flow<Boolean> {
        return remoteConfigDataSource.getFeatureFlagsFlow().map {
            it?.features
                ?.first { feature -> FeaturesEnum.ABOUT.value == feature.name }
                ?.isActive(sessionProvider.getVersion().toData())
                ?: false
        }
    }

    override fun updateApp(): AppUpdate? =
        remoteConfigDataSource.getFeatureFlags()?.update?.toDomain()

    private fun featureIsActive(feature: FeaturesEnum): Boolean {
        return remoteConfigDataSource
            .getFeatureFlags()
            ?.features
            ?.first { feature.value == it.name }
            ?.isActive(sessionProvider.getVersion().toData())
            ?: false
    }
}

enum class FeaturesEnum(val value: String) {
    ABOUT("feature_x"),
}
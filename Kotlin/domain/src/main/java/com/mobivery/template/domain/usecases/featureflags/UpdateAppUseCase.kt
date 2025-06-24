package com.mobivery.fct25.domain.usecases.featureflags

import com.mobivery.fct25.domain.model.featureflags.AppUpdate
import com.mobivery.fct25.domain.repository.FeatureFlagsRepository
import com.mobivery.fct25.domain.repository.SessionRepository
import javax.inject.Inject

class UpdateAppUseCase @Inject constructor(
    private val featureFlagsRepository: FeatureFlagsRepository,
    private val sessionRepository: SessionRepository,
) {

    operator fun invoke(): AppUpdate {
        val currentVersion = sessionRepository.getVersion()
        val appUpdate = featureFlagsRepository.updateApp() ?: AppUpdate(
            isActive = null,
            force = null,
            fromVersion = null,
            toVersion = null,
        )
        val isActive = if (appUpdate.isActive == null || appUpdate.fromVersion == null || appUpdate.toVersion == null) {
            false
        } else {
            appUpdate.isActive && currentVersion >= appUpdate.fromVersion && currentVersion <= appUpdate.toVersion
        }

        return appUpdate.copy(isActive = isActive)
    }
}

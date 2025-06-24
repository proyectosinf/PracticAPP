package com.mobivery.fct25.data.model

import com.mobivery.fct25.data.serializer.AppVersionSerializer
import com.mobivery.fct25.domain.model.featureflags.AppUpdate
import kotlinx.serialization.Serializable

/**
Represents the remote config feature flags from Firebase

## Firebase Config
Should be added to Firebase Remote config with the keys:
- `features_android` for production
- `features_android_pre` for development

```json
{
  "update": {
    "isActive": true,
    "force": false,
    "fromVersion": "0.0.0",
    "toVersion": "1.1.0"
  },
  "features": [
    {
      "name": "feature_x",
      "isActive": false,
      "fromVersion": "1.0.0",
      "toVersion": "2.1.0"
    }
  ]
}
```
*/

@Serializable
data class InfoRemoteConfig(
    val update: AppUpdateRemoteConfig,
    val features: List<FeatureFlagRemoteConfig>,
)

@Serializable
data class FeatureFlagRemoteConfig(
    val name: String?,
    val isActive: Boolean?,
    @Serializable(with = AppVersionSerializer::class)
    val fromVersion: AppVersionRemoteConfig?,
    @Serializable(with = AppVersionSerializer::class)
    val toVersion: AppVersionRemoteConfig?,
) {
    fun isActive(version: AppVersionRemoteConfig): Boolean {
        if (isActive == null || fromVersion == null || toVersion == null) {
            return false
        }
        return isActive && version >= fromVersion && version <= toVersion
    }
}

@Serializable
data class AppUpdateRemoteConfig(
    val isActive: Boolean?,
    val force: Boolean?,
    @Serializable(with = AppVersionSerializer::class)
    val fromVersion: AppVersionRemoteConfig?,
    @Serializable(with = AppVersionSerializer::class)
    val toVersion: AppVersionRemoteConfig?,
) {
    fun toDomain() =
        AppUpdate(
            isActive = isActive,
            force = force,
            fromVersion = fromVersion?.toDomain(),
            toVersion = toVersion?.toDomain()
        )
}

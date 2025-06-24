package com.mobivery.fct25.data.datasources

import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.mobivery.fct25.data.BuildConfig
import com.mobivery.fct25.data.R
import com.mobivery.fct25.data.extension.TAG
import com.mobivery.fct25.data.model.InfoRemoteConfig
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.Json
import javax.inject.Inject

private const val FEATURE_FLAG_PRE_KEY = "features_android_pre"
private const val FEATURE_FLAG_KEY = "features_android"

class RemoteConfigDataSource @Inject constructor() {

    suspend fun init() {
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        if (BuildConfig.DEBUG) {
            val configSettings = remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0
            }
            remoteConfig.setConfigSettingsAsync(configSettings)
        }
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        remoteConfig.fetchAndActivate().await()
    }

    fun getFeatureFlags(): InfoRemoteConfig? {
        val featureFlags = Firebase.remoteConfig.getString(
            if (BuildConfig.DEBUG) {
                FEATURE_FLAG_PRE_KEY
            } else {
                FEATURE_FLAG_KEY
            }
        )
        return try {
            Json.decodeFromString<InfoRemoteConfig>(featureFlags)
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "Feature flags JSON decoding error")
            }
            null
        }
    }

    fun getFeatureFlagsFlow(): Flow<InfoRemoteConfig?> = callbackFlow {

        val remoteConfig = Firebase.remoteConfig
        val featureFlagKey = if (BuildConfig.DEBUG) {
            FEATURE_FLAG_PRE_KEY
        } else {
            FEATURE_FLAG_KEY
        }

        // Send initial value
        trySend(getFeatureFlags())

        val listener = object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                if (configUpdate.updatedKeys.contains(featureFlagKey)) {
                    remoteConfig.activate().addOnCompleteListener {
                        trySend(getFeatureFlags())
                    }
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "Error updating feature flags: ${error.message}")
                }
            }
        }
        val listenerRegistration = remoteConfig.addOnConfigUpdateListener(listener)

        // Clean up when the flow collector is cancelled
        awaitClose {
            listenerRegistration.remove()
        }
    }
}

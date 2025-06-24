package com.mobivery.fct25.app.common.analytics

import android.content.Context
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.Firebase
import com.mobivery.fct25.data.BuildConfig
import com.amplitude.android.Amplitude
import com.amplitude.android.Configuration
import com.amplitude.core.ServerZone
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import org.json.JSONObject

class AnalyticsHelper(private val context: Context) {

    private val firebaseAnalytics = Firebase.analytics

    private val amplitudeAnalytics = Amplitude(
        Configuration(
            apiKey = BuildConfig.AMPLITUDE_KEY,
            context = context,
            // TODO: For EU data residency, the project must be set up inside Amplitude EU (https://amplitude.com/docs/sdks/analytics/android/android-kotlin-sdk#eu-data-residency)
            serverZone = ServerZone.EU
        )
    )

    fun setUserId(userId: String?) {
        firebaseAnalytics.setUserId(userId)
        amplitudeAnalytics.setUserId(userId)
    }

    private fun logEvent(eventName: String, eventAttributes: JSONObject? = null) {
        if (eventAttributes != null) {
            firebaseAnalytics.logEvent(eventName) {
                for (key in eventAttributes.keys()) {
                    when (eventAttributes.get(key)) {
                        is String -> param(key, eventAttributes.get(key) as String)
                        is Int -> param(key, (eventAttributes.get(key) as Int).toLong())
                    }
                }
            }

            amplitudeAnalytics.track(
                eventName, Gson().fromJson(
                    eventAttributes.toString(),
                    object : TypeToken<Map<String, Any>>() {}.type
                )
            )
        } else {
            firebaseAnalytics.logEvent(eventName) { /* no attributes */ }
            amplitudeAnalytics.track(eventName)
        }
    }
}

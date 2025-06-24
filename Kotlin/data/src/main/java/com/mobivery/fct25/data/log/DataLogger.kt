package com.mobivery.fct25.data.log

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import com.mobivery.fct25.data.BuildConfig

internal class DataLogger {
    companion object {
        fun log(
            tag: String,
            message: String,
            logType: LogType = LogType.WARNING,
            error: Throwable? = null
        ) {
            if (BuildConfig.DEBUG) {
                Log.d(tag, message)
            }
            if (logType == LogType.ERROR && error != null) {
                // Send to Firebase non fatal errors
                Firebase.crashlytics.recordException(error)
            }
        }
    }

    enum class LogType {
        ERROR,
        WARNING,
        INFO
    }
}

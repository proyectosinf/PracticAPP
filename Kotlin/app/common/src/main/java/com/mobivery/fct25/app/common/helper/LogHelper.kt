package com.mobivery.fct25.app.common.helper

import com.mobivery.fct25.app.common.BuildConfig
import android.util.Log

class LogHelper {

    companion object {
        fun log(tag: String, message: String) {
            if (BuildConfig.DEBUG) {
                Log.d(tag, message)
            }
        }
    }
}
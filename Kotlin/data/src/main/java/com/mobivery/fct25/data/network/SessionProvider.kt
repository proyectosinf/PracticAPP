package com.mobivery.fct25.data.network

import com.mobivery.fct25.domain.model.featureflags.AppVersion

interface SessionProvider {

    fun getLanguage(): String

    fun getVersion(): AppVersion

    fun getVersionCode(): Int

    fun getFullVersion(): String
}
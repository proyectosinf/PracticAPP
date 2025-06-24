package com.mobivery.fct25.data.network

import com.mobivery.fct25.data.BuildConfig
import com.mobivery.fct25.domain.model.featureflags.AppVersion
import java.util.Locale

class SessionProviderImpl : SessionProvider {

    override fun getLanguage(): String =
        Locale.getDefault().language

    override fun getVersion(): AppVersion =
        AppVersion(BuildConfig.VERSION_NAME)

    override fun getVersionCode(): Int =
        BuildConfig.VERSION_CODE

    override fun getFullVersion(): String =
        "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
}

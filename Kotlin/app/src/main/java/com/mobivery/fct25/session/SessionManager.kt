package com.mobivery.fct25.session

import android.content.Context
import com.mobivery.fct25.BuildConfig
import com.mobivery.fct25.R
import com.mobivery.fct25.data.network.SessionProvider
import com.mobivery.fct25.domain.model.featureflags.AppVersion

class SessionManager(
    private val context: Context,
) : SessionProvider {

    override fun getLanguage() = context.getString(R.string.common_language_code)

    override fun getVersion() = AppVersion(BuildConfig.VERSION_NAME)

    override fun getVersionCode() = BuildConfig.VERSION_CODE

    override fun getFullVersion() = "${BuildConfig.VERSION_NAME}-${BuildConfig.VERSION_CODE}"
}
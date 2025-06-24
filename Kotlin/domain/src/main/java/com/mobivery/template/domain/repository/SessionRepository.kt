package com.mobivery.fct25.domain.repository

import com.mobivery.fct25.domain.model.featureflags.AppVersion

interface SessionRepository {

    fun getVersion(): AppVersion

    fun saveTokens(accessToken: String, refreshToken: String?)

    fun clearTokens()
}

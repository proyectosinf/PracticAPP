package com.mobivery.fct25.data.repository

import com.mobivery.fct25.data.network.SessionProvider
import com.mobivery.fct25.data.preferences.EncryptedPreferences
import com.mobivery.fct25.domain.model.featureflags.AppVersion
import com.mobivery.fct25.domain.repository.SessionRepository
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
    private val sessionProvider: SessionProvider,
    private val encryptedPreferences: EncryptedPreferences
) : SessionRepository {

    override fun getVersion(): AppVersion = sessionProvider.getVersion()

    override fun saveTokens(accessToken: String, refreshToken: String?) {
        encryptedPreferences.saveTokens(accessToken, refreshToken)
    }

    override fun clearTokens() {
        encryptedPreferences.clearTokens()
    }
}

package com.mobivery.fct25.data.datasources

import com.mobivery.fct25.data.local.preferences.model.UserLocal
import com.mobivery.fct25.data.preferences.DataStorePreferences
import com.mobivery.fct25.data.preferences.EncryptedPreferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PreferencesDataSource @Inject constructor(
    private val encryptedPreferences: EncryptedPreferences,
    private val dataStorePreferences: DataStorePreferences,
) {
    // Save sensitive data only in EncryptedPreferences

    fun saveAccessToken(accessToken: String) = encryptedPreferences.saveAccessToken(accessToken)

    fun getAccessToken() = encryptedPreferences.getAccessToken()

    fun saveRefreshToken(refreshToken: String) = encryptedPreferences.saveRefreshToken(refreshToken)

    fun getRefreshToken() = encryptedPreferences.getRefreshToken()

    fun saveFirebaseToken(token: String) = encryptedPreferences.saveFirebaseToken(token)

    fun getFirebaseToken(): String? = encryptedPreferences.getFirebaseToken()

    suspend fun logout() {
        encryptedPreferences.logout()
        removeUser()
    }

    suspend fun saveUser(user: UserLocal) = dataStorePreferences.saveUser(user)

    fun getUser() = dataStorePreferences.getUser()

    suspend fun removeUser() = dataStorePreferences.removeUser()

    fun getDarkMode(): Flow<Boolean> = dataStorePreferences.getDarkMode()

    suspend fun saveDarkMode(isEnable: Boolean) = dataStorePreferences.saveDarkMode(isEnable)
}

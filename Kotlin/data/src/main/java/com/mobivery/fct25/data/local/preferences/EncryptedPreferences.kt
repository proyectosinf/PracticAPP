package com.mobivery.fct25.data.preferences

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.mobivery.fct25.data.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

private const val FILE_NAME = "encrypted_preferences_fct25"
private const val ACCESS_TOKEN_KEY = "access_token_key"
private const val REFRESH_TOKEN_KEY = "refresh_token_key"
private const val FIREBASE_TOKEN_KEY = "firebase_token_key"
private const val LOG_TAG = "EncryptedPreferences"
private const val DELETION_LOG_MESSAGE = "Cannot retrieve preferences encrypted with current master key. Deleting and recreating."

@Singleton
class EncryptedPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getEncryptedSharedPreferences(FILE_NAME)
    }

    fun saveAccessToken(accessToken: String) {
        sharedPreferences.edit()
            .putString(ACCESS_TOKEN_KEY, accessToken)
            .apply()
    }

    fun saveRefreshToken(refreshToken: String) {
        sharedPreferences.edit()
            .putString(REFRESH_TOKEN_KEY, refreshToken)
            .apply()
    }

    fun saveTokens(accessToken: String, refreshToken: String?) {
        sharedPreferences.edit().apply {
            putString(ACCESS_TOKEN_KEY, accessToken)
            if (!refreshToken.isNullOrBlank()) {
                putString(REFRESH_TOKEN_KEY, refreshToken)
            }
            apply()
        }
    }

    fun getAccessToken(): String? =
        sharedPreferences.getString(ACCESS_TOKEN_KEY, null)

    fun getRefreshToken(): String? =
        sharedPreferences.getString(REFRESH_TOKEN_KEY, null)

    fun saveFirebaseToken(token: String) {
        sharedPreferences.edit()
            .putString(FIREBASE_TOKEN_KEY, token)
            .apply()
    }

    fun getFirebaseToken(): String? =
        sharedPreferences.getString(FIREBASE_TOKEN_KEY, null)

    fun clearTokens() {
        sharedPreferences.edit()
            .remove(ACCESS_TOKEN_KEY)
            .remove(REFRESH_TOKEN_KEY)
            .apply()
    }

    fun logout() {
        sharedPreferences.edit()
            .clear()
            .apply()
    }
}

fun Context.getEncryptedSharedPreferences(filename: String): SharedPreferences {
    val masterKey = MasterKey.Builder(this)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    val sharedPreferences = getEncryptedSharedPreferencesOrNull(filename, masterKey)
    return sharedPreferences ?: run {
        if (BuildConfig.DEBUG) {
            Log.d(LOG_TAG, DELETION_LOG_MESSAGE)
        }
        deleteSharedPreferences(filename)
        getEncryptedSharedPreferencesOrThrow(filename, masterKey)
    }
}

fun Context.getEncryptedSharedPreferencesOrNull(
    filename: String,
    key: MasterKey
): SharedPreferences? = try {
    getEncryptedSharedPreferencesOrThrow(filename, key)
} catch (_: Exception) {
    null
}

fun Context.getEncryptedSharedPreferencesOrThrow(
    filename: String,
    key: MasterKey
): SharedPreferences =
    EncryptedSharedPreferences.create(
        this,
        filename,
        key,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

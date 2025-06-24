package com.mobivery.fct25.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mobivery.fct25.data.local.preferences.model.UserLocal
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "fct25DataStore")

class DataStorePreferences @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private companion object {
        val USER_KEY = stringPreferencesKey("userKey")
        val DARK_MODE_KEY = booleanPreferencesKey("darkModeKey")
    }

    suspend fun saveUser(user: UserLocal) {
        context.dataStore.edit { dataStore ->
            dataStore[USER_KEY] = Json.encodeToString(user)
        }
    }

    fun getUser() : Flow<UserLocal?> = context.dataStore.data.map { dataStore ->
        try {
            Json.decodeFromString<UserLocal>(
                dataStore[USER_KEY] ?: throw NoSuchElementException()
            )
        } catch (e: Exception) {
            null
        }
    }

    suspend fun removeUser() = context.dataStore.edit { dataStore ->
        dataStore.remove(USER_KEY)
    }

    fun getDarkMode(): Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[DARK_MODE_KEY] ?: false
        }

    suspend fun saveDarkMode(isEnable: Boolean) {
        context.dataStore.edit { dataStore ->
            dataStore[DARK_MODE_KEY] = isEnable
        }
    }
}
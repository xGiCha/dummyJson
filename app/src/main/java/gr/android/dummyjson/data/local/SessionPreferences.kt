package gr.android.dummyjson.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import gr.android.dummyjson.utils.Constants.ACCESS_TOKEN
import gr.android.dummyjson.utils.Constants.REFRESH_TOKEN
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionPreferences(private val dataStore: DataStore<Preferences>) {

    suspend fun saveAccessToken(accessToken: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken
        }
    }

    val getAccessToken: Flow<String> = dataStore.data.map { preferences ->
        preferences[ACCESS_TOKEN] ?: ""
    }

    suspend fun saveRefreshToken(refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN] = refreshToken
        }
    }

    val getRefreshToken: Flow<String> = dataStore.data.map { preferences ->
        preferences[REFRESH_TOKEN] ?: ""
    }

    suspend fun clearTokens() {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = ""
            preferences[REFRESH_TOKEN] = ""
        }
    }
}
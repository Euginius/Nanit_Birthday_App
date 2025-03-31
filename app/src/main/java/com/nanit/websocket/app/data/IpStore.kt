package com.nanit.websocket.app.data


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface IpStore {
    fun getLastUsedIp(): Flow<String?>   // Observe IP changes
    suspend fun saveLastUsedIp(ip: String)
    suspend fun clearLastUsedIp()
}

class IpStoreImpl(private val dataStore: DataStore<Preferences>) : IpStore {

    companion object {
        private val IP_KEY = stringPreferencesKey("last_used_ip")
    }

    override fun getLastUsedIp(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[IP_KEY]
        }
    }

    override suspend fun saveLastUsedIp(ip: String) {
        dataStore.edit { preferences ->
            preferences[IP_KEY] = ip
        }
    }

    override suspend fun clearLastUsedIp() {
        dataStore.edit { preferences ->
            preferences.remove(IP_KEY)
        }
    }
}
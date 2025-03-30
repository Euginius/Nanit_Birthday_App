package com.nanit.websocket.app.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface IpStore {
    fun getLastUsedIp(): Flow<String?>   // Observe IP changes
    suspend fun saveLastUsedIp(ip: String)
    suspend fun clearLastUsedIp()
}

private val Context.dataStore by preferencesDataStore(name = "ip_store")

class IpStoreImpl(private val context: Context) : IpStore {

    companion object {
        private val IP_KEY = stringPreferencesKey("last_used_ip")
    }

    override fun getLastUsedIp(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[IP_KEY]
        }
    }

    override suspend fun saveLastUsedIp(ip: String) {
        context.dataStore.edit { preferences ->
            preferences[IP_KEY] = ip
        }
    }

    override suspend fun clearLastUsedIp() {
        context.dataStore.edit { preferences ->
            preferences.remove(IP_KEY)
        }
    }
}
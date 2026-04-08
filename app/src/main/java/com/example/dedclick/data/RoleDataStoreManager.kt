package com.example.dedclick.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoleDataStoreManager(private val context : Context) : DataStoreManager {
    override suspend fun setValue(value: String) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.ROLE] = value
        }
    }

    override fun getValue(): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[PreferencesKeys.ROLE]
        }
    }
}
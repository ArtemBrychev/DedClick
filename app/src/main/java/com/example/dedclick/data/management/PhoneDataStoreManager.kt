package com.example.dedclick.data.management

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.dedclick.data.management.DataStoreManager
import com.example.dedclick.data.PreferencesKeys
import com.example.dedclick.data.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PhoneDataStoreManager(private val context : Context) : DataStoreManager {
    override suspend fun setValue(value: String) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.PHONE] = value
        }
    }

    override fun getValue(): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[PreferencesKeys.PHONE]
        }
    }
}
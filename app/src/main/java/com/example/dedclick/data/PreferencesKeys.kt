package com.example.dedclick.data

import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val JWT = stringPreferencesKey("jwt")
    val ROLE = stringPreferencesKey("role")
    val NAME = stringPreferencesKey("name")
    val PHONE = stringPreferencesKey("phone")
}
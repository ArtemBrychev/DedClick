package com.example.dedclick.data.management

import kotlinx.coroutines.flow.Flow

interface DataStoreManager {

    suspend fun setValue(value: String)
    fun getValue(): Flow<String?>
}
package com.example.dedclick.data

import kotlinx.coroutines.flow.Flow

interface DataStoreManager {

    suspend fun setValue(value: String)
    fun getValue(): Flow<String?>
}
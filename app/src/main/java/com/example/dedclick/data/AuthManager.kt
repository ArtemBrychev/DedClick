package com.example.dedclick.data

import android.content.Context
import kotlinx.coroutines.flow.Flow

class AuthManager(val context : Context) {

    private val roleDataStoreManager = RoleDataStoreManager(context)
    private val tokenDataStoreManager = TokenDataStoreManager(context)

    fun getToken() : Flow<String?>{
        return tokenDataStoreManager.getValue()
    }

    suspend fun setRole(role:String){
        roleDataStoreManager.setValue(role)
    }

    //... и так далее

}

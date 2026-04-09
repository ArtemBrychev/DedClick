package com.example.dedclick.data

import android.content.Context
import com.example.dedclick.data.management.*
import com.example.dedclick.data.model.UserAuthInfo
import kotlinx.coroutines.flow.first

class AuthManager(context: Context) {

    private val roleDataStoreManager = RoleDataStoreManager(context)
    private val tokenDataStoreManager = TokenDataStoreManager(context)
    private val nameDataStoreManager = NameDataStoreManager(context)
    private val phoneDataStoreManager = PhoneDataStoreManager(context)

    // --------------------
    // ПРИВАТНЫЕ МЕТОДЫ
    // --------------------

    private suspend fun getToken(): String? {
        return tokenDataStoreManager.getValue().first()
    }

    private suspend fun setToken(token: String) {
        tokenDataStoreManager.setValue(token)
    }

    private suspend fun getName(): String? {
        return nameDataStoreManager.getValue().first()
    }

    private suspend fun setName(name: String) {
        nameDataStoreManager.setValue(name)
    }

    private suspend fun getPhone(): String? {
        return phoneDataStoreManager.getValue().first()
    }

    private suspend fun setPhone(phone: String) {
        phoneDataStoreManager.setValue(phone)
    }

    private suspend fun getRole(): String? {
        return roleDataStoreManager.getValue().first()
    }

    private suspend fun setRole(role: String) {
        roleDataStoreManager.setValue(role)
    }

    // --------------------
    // ПУБЛИЧНЫЕ МЕТОДЫ
    // --------------------

    suspend fun saveUserAuthInfo(userAuthInfo: UserAuthInfo) {
        setToken(userAuthInfo.token)
        setName(userAuthInfo.name)
        setPhone(userAuthInfo.phone)
        setRole(userAuthInfo.role)
    }

    suspend fun getUserAuthInfo(): UserAuthInfo? {

        val token = getToken()
        val name = getName()
        val phone = getPhone()
        val role = getRole()

        if (token == null || token=="" || name == null || phone == null || role == null) {
            return null
        }

        return UserAuthInfo(
            token = token,
            name = name,
            phone = phone,
            role = role
        )
    }

    suspend fun clearAuthInfo() {
        setToken("")
        setName("")
        setPhone("")
        setRole("")
    }
}
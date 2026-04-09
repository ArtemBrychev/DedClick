package com.example.dedclick.service

import com.example.dedclick.data.model.UserRegister
import com.example.dedclick.data.model.UserTokenRequest

object AuthApiProvider {

    private val authService: AuthService by lazy {
        RetrofitProvider.retrofit.create(AuthService::class.java)
    }

    suspend fun register(
        phone: String,
        username: String,
        role: String
    ): ApiResult<Unit> {

        val roleForBackend = if (role == "elder") "MEMBER" else "KEEPER"
        val userRegister = UserRegister(phone, username, roleForBackend)

        return try {
            val response = authService.register(userRegister)

            if (response.isSuccessful) {
                ApiResult.Success(Unit)
            } else {
                ApiResult.Error(
                    response.code(),
                    response.errorBody()?.string()
                )
            }

        } catch (e: Exception) {
            ApiResult.Error(-1, e.message)
        }
    }

    suspend fun generateCode(phone: String): ApiResult<Unit> {
        return try {
            val response = authService.generateCode(phone)

            if (response.isSuccessful) {
                ApiResult.Success(Unit)
            } else {
                ApiResult.Error(
                    response.code(),
                    response.errorBody()?.string()
                )
            }

        } catch (e: Exception) {
            ApiResult.Error(-1, e.message)
        }
    }

    suspend fun login(phone: String, code: String): ApiResult<String> {

        val tokenRequest = UserTokenRequest(phone, code)

        return try {
            val response = authService.login(tokenRequest)

            if (response.isSuccessful) {

                val token = response.body()?.token

                if (token != null) {
                    ApiResult.Success(token)
                } else {
                    ApiResult.Error(-1, "Empty response body")
                }

            } else {
                ApiResult.Error(
                    response.code(),
                    response.errorBody()?.string()
                )
            }

        } catch (e: Exception) {
            ApiResult.Error(-1, e.message)
        }
    }
}
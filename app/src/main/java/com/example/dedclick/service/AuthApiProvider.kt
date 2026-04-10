package com.example.dedclick.service

import android.util.Log
import com.example.dedclick.BuildConfig
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
            Log.i("AUTH:API:PROVIDER", "Request sent to: ${BuildConfig.BASE_URL} register(phone: $phone) ")
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
            Log.i("AUTH:API:PROVIDER", "Request sent to: ${BuildConfig.BASE_URL} generateCode(phone: $phone) ")
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
            Log.i("AUTH:API:PROVIDER", "Request sent to: ${BuildConfig.BASE_URL} login(phone: $phone) ")
            val response = authService.login(tokenRequest)

            if (response.isSuccessful) {
                val token = response.body()?.token

                if (token != null) {
                    Log.i("AUTH:API:PROVIDER", "Получен токен: $token")
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
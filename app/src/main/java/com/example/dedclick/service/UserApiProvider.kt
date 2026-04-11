package com.example.dedclick.service

import android.util.Log
import com.example.dedclick.data.model.UserDto

object UserApiProvider {

    private val userService: UserService by lazy {
        RetrofitProvider.retrofit.create(UserService::class.java)
    }

    suspend fun getUserSelfInfo(token: String): ApiResult<UserDto> {
        return try {
            Log.i("USER:API:PROVIDER", "Request sent to: getCurrentUserInfo")

            val response = userService.getCurrentUserInfo("Bearer $token")

            if (response.isSuccessful) {
                val user = response.body()

                if (user != null) {
                    user.roleName = if(user.roleName=="MEMBER") "elder" else "trusted"
                    ApiResult.Success(user)
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
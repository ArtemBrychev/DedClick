package com.example.dedclick.service

import com.example.dedclick.data.model.UserRegister
import com.example.dedclick.data.model.UserTokenRequest
import com.example.dedclick.data.model.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {
    @POST("/api/v1/auth/generate")
    suspend fun generateCode(
        @Query("username") username: String
    ): Response<Unit>

    @POST("/api/v1/auth/register")
    suspend fun register(
        @Body newUserRequest: UserRegister
    ): Response<Unit>

    @POST("/api/v1/auth/login")
    suspend fun login(
        @Body loginRequest: UserTokenRequest
    ): Response<TokenResponse>
}
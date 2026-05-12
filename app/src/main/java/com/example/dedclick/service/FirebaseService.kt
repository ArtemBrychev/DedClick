package com.example.dedclick.service

import com.example.dedclick.data.model.FirebaseToken
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface FirebaseService {

    @POST("/api/v1/push-token/send")
    suspend fun sendToken(
        @Header("Authorization") token: String,
        @Body firebaseToken: FirebaseToken
    ): Response<Unit>
}
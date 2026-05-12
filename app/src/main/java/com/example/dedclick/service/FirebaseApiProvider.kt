package com.example.dedclick.service

import android.util.Log
import com.example.dedclick.data.model.FirebaseToken

object FirebaseApiProvider {

    private val firebaseService: FirebaseService by lazy {
        RetrofitProvider.retrofit.create(FirebaseService::class.java)
    }

    suspend fun sendFirebaseToken(usertoken: String, username:String, firetoken: String): ApiResult<Unit>{
        val request = FirebaseToken(username, firetoken, "Android")
        return try {
            val response = firebaseService.sendToken(
                "Bearer $usertoken",
                request
            )

            Log.i("SEND:FIREBASE:TOKEN", "usertoken=$usertoken")
            Log.i("SEND:FIREBASE:TOKEN", "Sending token(${request.token}) to ${request.username}")
            Log.i("SEND:FIREBASE:TOKEN", "FUll REQUEST: ${request.toString()}")
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
}
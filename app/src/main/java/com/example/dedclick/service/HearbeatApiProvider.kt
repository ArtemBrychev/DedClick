package com.example.dedclick.service

import android.util.Log
import com.example.dedclick.data.model.HeartbeatDto
import com.example.dedclick.data.model.HeartbeatTapRequest

object HeartbeatApiProvider {

    private val heartbeatService: HeartbeatService by lazy {
        RetrofitProvider.retrofit.create(HeartbeatService::class.java)
    }

    suspend fun sendHeartbeat(token: String, lat:Double?, lon:Double?): ApiResult<Unit>{
        return try{
            val response = heartbeatService.sendHeartbeat("Bearer $token", HeartbeatTapRequest(lat, lon))
            if(response.isSuccessful){
                ApiResult.Success(Unit)
            }else{
                ApiResult.Error(response.code(), response.message())
            }
        }catch (e: Exception){
            ApiResult.Error(-1, "Ошибка при отправке запроса")
        }
    }

    suspend fun getSelfHeartbeat(token: String): ApiResult<HeartbeatDto>{
        return try{
            val response = heartbeatService.getSelfHeartbeat("Bearer $token")
            if(response.isSuccessful){
                if(response.body() != null){
                    return ApiResult.Success(response.body())
                }else{
                    Log.e("GET:SELF:HEARTBET", "ААААААА ПУСТОЕ ТЕЛО ПУСТОЕ ТЕЛО ПУСТОЕ ТЕЛО ПУСТОЕ ТЕЛО")
                    return ApiResult.Error(-1, "Empty body")
                }
            }else{
                ApiResult.Error(response.code(), response.message())
            }
        }catch (e: Exception){
            ApiResult.Error(-1, "Ошибка при отправке запроса")
        }
    }

    suspend fun getHeartbeat(id:Long, token: String): ApiResult<HeartbeatDto>{
        return try{
            val response = heartbeatService.getHeartbeatById(id, "Bearer $token")
            if(response.isSuccessful){
                if(response.body() != null){
                    return ApiResult.Success(response.body())
                }else{
                    Log.e("GET:SELF:HEARTBET", "ААААААА ПУСТОЕ ТЕЛО ПУСТОЕ ТЕЛО ПУСТОЕ ТЕЛО ПУСТОЕ ТЕЛО")
                    return ApiResult.Error(-1, "Empty body")
                }
            }else{
                ApiResult.Error(response.code(), response.message())
            }
        }catch (e: Exception){
            ApiResult.Error(-1, "Ошибка при отправке запроса")
        }
    }
}
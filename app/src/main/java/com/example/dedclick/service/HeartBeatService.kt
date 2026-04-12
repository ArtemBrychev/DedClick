package com.example.dedclick.service

import com.example.dedclick.data.model.HeartbeatDto
import com.example.dedclick.data.model.HeartbeatTapRequest
import retrofit2.Response
import retrofit2.http.*

interface HeartbeatService {

    // Получение собственного heartbeat (только для MEMBER)
    // Возвращает текущий лог пульса пользователя
    @GET("/api/v1/heartbeat/self")
    suspend fun getSelfHeartbeat(
        @Header("Authorization") token: String
    ): Response<HeartbeatDto>


    // Получение heartbeat другого пользователя по id (только для KEEPER)
    // Требуется наличие контакта между пользователями
    @GET("/api/v1/heartbeat/{id}")
    suspend fun getHeartbeatById(
        @Path("id") id: Long,
        @Header("Authorization") token: String
    ): Response<HeartbeatDto>


    // Отправка "пульса" (нажатие кнопки "я в порядке")
    // Обновляет время и при наличии координаты пользователя
    @PUT("/api/v1/heartbeat/tap")
    suspend fun sendHeartbeat(
        @Header("Authorization") token: String,
        @Body body: HeartbeatTapRequest
    ): Response<Unit>
}
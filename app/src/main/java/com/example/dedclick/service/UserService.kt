package com.example.dedclick.service

import com.example.dedclick.data.model.UpdatePositionRequest
import com.example.dedclick.data.model.UserDto
import com.example.dedclick.data.model.UserShortDto
import retrofit2.Response
import retrofit2.http.*

interface UserService {

    // Получение полной информации о текущем пользователе
    @GET("/api/v1/users/self")
    suspend fun getCurrentUserInfo(
        @Header("Authorization") token: String
    ): Response<UserDto>


    // Получение полной информации о пользователе по id
    @GET("/api/v1/users/{id}")
    suspend fun getUserInfo(
        @Path("id") id: Long,
        @Header("Authorization") token: String
    ): Response<UserDto>


    // Получение списка всех пользователей с ролью MEMBER (короткая модель)
    @GET("/api/v1/users/member")
    suspend fun getMembers(
        @Header("Authorization") token: String
    ): Response<List<UserShortDto>>


    // Удаление текущего пользователя
    @DELETE("/api/v1/users/delete")
    suspend fun deleteSelf(
        @Header("Authorization") token: String
    ): Response<Unit>


    // Удаление пользователя по id
    @DELETE("/api/v1/users/delete/{id}")
    suspend fun deleteById(
        @Path("id") id: Long,
        @Header("Authorization") token: String
    ): Response<Unit>


    // Обновление позиции пользователя
    @PATCH("/api/v1/users/pos")
    suspend fun updatePosition(
        @Header("Authorization") token: String,
        @Body body: UpdatePositionRequest
    ): Response<Unit>


    // Обновление времени активности пользователя
    @PATCH("/api/v1/users/active")
    suspend fun updateActivity(
        @Header("Authorization") token: String
    ): Response<Unit>
}
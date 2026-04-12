package com.example.dedclick.service

import com.example.dedclick.data.model.AddContactRequest
import com.example.dedclick.data.model.ContactDto
import retrofit2.Response
import retrofit2.http.*

interface ContactService {

    // Получение списка всех контактов текущего пользователя
    // Возвращает список контактов (может быть пустым)
    @GET("/api/v1/contacts/get")
    suspend fun getContacts(
        @Header("Authorization") token: String
    ): Response<List<ContactDto>>


    // Отправка заявки на добавление контакта (только для KEEPER)
    // В body передается username (номер телефона) пожилого пользователя
    @POST("/api/v1/contacts/add")
    suspend fun addContact(
        @Header("Authorization") token: String,
        @Body body: AddContactRequest
    ): Response<Unit>


    // Принятие заявки на контакт (только для MEMBER)
    // id — id контакта (заявки)
    @PATCH("/api/v1/contacts/{id}/respond")
    suspend fun respondContact(
        @Path("id") id: Long,
        @Header("Authorization") token: String
    ): Response<Unit>


    // Удаление контакта
    // Может быть вызвано либо владельцем, либо участником контакта
    @DELETE("/api/v1/contacts/{id}/delete")
    suspend fun deleteContact(
        @Path("id") id: Long,
        @Header("Authorization") token: String
    ): Response<Unit>
}
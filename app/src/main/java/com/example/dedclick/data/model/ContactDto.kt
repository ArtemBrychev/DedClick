package com.example.dedclick.data.model

data class ContactDto(
    val id: Long,
    val keeper: UserShortDto,
    val member: UserShortDto,
    val status: Int,
    val createdAt: String,
    val respondedAt: String?
)
package com.example.dedclick.data.model

data class HeartbeatDto(
    val id: Long,
    val user: UserShortDto,
    val tappedAt: String,
    val lat: Double?,
    val lon: Double?
)
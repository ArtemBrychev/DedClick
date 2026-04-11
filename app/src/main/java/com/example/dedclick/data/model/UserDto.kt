package com.example.dedclick.data.model

data class UserDto(
    val id: Long,
    val username: String,
    val fullName: String,
    var roleName: String,
    val lastKnownLat: Double?,
    val lastKnownLon: Double?,
    val lastKnownAt: String?,
    val lastHeartbeatAt: String?,
    val reminderSentAt: String?,
    val lastActiveAt: String?,
    val registeredAt: String
)
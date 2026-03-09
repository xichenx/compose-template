package com.xichen.dev.data.remote.model

data class LoginResponseDto(
    val userId: String,
    val displayName: String,
    val email: String,
    val token: String
)

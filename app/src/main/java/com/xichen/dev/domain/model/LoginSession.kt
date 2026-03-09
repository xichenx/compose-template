package com.xichen.dev.domain.model

data class LoginSession(
    val userId: String,
    val displayName: String,
    val email: String,
    val token: String
)

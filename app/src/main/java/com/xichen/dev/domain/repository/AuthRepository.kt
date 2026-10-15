package com.xichen.dev.domain.repository

import com.xichen.dev.domain.model.AppResult
import com.xichen.dev.domain.model.LoginSession

interface AuthRepository {
    suspend fun login(email: String, password: String): AppResult<LoginSession>
    suspend fun register(
        displayName: String,
        email: String,
        password: String
    ): AppResult<LoginSession>
    suspend fun getCachedSession(): LoginSession?
    suspend fun clearSession()
}

package com.xichen.dev.data.remote.api

import com.xichen.dev.data.remote.model.LoginRequestDto
import com.xichen.dev.data.remote.model.LoginResponseDto
import com.xichen.dev.data.remote.model.RegisterRequestDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(@Body body: LoginRequestDto): LoginResponseDto

    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequestDto): LoginResponseDto
}

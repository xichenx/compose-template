package com.xichen.dev.data.remote.api

import retrofit2.http.GET

interface ScaffoldApiService {
    @GET("scaffold/highlights")
    suspend fun getHighlights(): List<Map<String, String>>
}

package com.xichen.dev.domain.repository

import com.xichen.dev.domain.model.AppLanguage
import com.xichen.dev.domain.model.AppResult

interface LanguageRepository {
    suspend fun getAppLanguage(): AppResult<AppLanguage>
    suspend fun updateAppLanguage(language: AppLanguage): AppResult<Unit>
    suspend fun initializeAppLanguage(): AppResult<Unit>
}

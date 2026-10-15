package com.xichen.dev.domain.repository

import com.xichen.dev.domain.model.AppLanguage

interface LanguageRepository {
    suspend fun getAppLanguage(): AppLanguage
    suspend fun updateAppLanguage(language: AppLanguage)
    suspend fun initializeAppLanguage()
}

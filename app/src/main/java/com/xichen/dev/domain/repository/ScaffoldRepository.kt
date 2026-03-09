package com.xichen.dev.domain.repository

import com.xichen.dev.domain.model.AppThemeMode
import com.xichen.dev.domain.model.AppResult
import com.xichen.dev.domain.model.ScaffoldSummary
import kotlinx.coroutines.flow.Flow

interface ScaffoldRepository {
    suspend fun getScaffoldSummary(forceRefresh: Boolean = false): AppResult<ScaffoldSummary>
    suspend fun markSetupGuideAsSeen()
    fun observeAppThemeMode(): Flow<AppThemeMode>
    suspend fun updateAppThemeMode(themeMode: AppThemeMode)
}

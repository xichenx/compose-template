package com.xichen.dev.data.repository

import com.xichen.dev.core.coroutine.AppDispatchers
import com.xichen.dev.data.common.appResultOf
import com.xichen.dev.data.local.datasource.ScaffoldLocalDataSource
import com.xichen.dev.data.local.datastore.ScaffoldPreferencesDataSource
import com.xichen.dev.data.local.mapper.toDomain
import com.xichen.dev.data.local.mapper.toEntity
import com.xichen.dev.data.remote.datasource.ScaffoldRemoteDataSource
import com.xichen.dev.data.remote.mapper.toDomain
import com.xichen.dev.domain.model.AppError
import com.xichen.dev.domain.model.AppThemeMode
import com.xichen.dev.domain.model.AppResult
import com.xichen.dev.domain.model.ScaffoldSummary
import com.xichen.dev.domain.repository.ScaffoldRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ScaffoldRepositoryImpl @Inject constructor(
    private val remoteDataSource: ScaffoldRemoteDataSource,
    private val localDataSource: ScaffoldLocalDataSource,
    private val preferencesDataSource: ScaffoldPreferencesDataSource,
    private val dispatchers: AppDispatchers
) : ScaffoldRepository {

    override fun observeAppThemeMode(): Flow<AppThemeMode> {
        return preferencesDataSource.observeAppThemeMode()
    }

    override suspend fun getScaffoldSummary(forceRefresh: Boolean): AppResult<ScaffoldSummary> {
        val result = withContext(dispatchers.io) {
            appResultOf {
                val cachedHighlights = localDataSource.getHighlights()
                if (cachedHighlights.isNotEmpty() && !forceRefresh) {
                    val preferences = preferencesDataSource.getPreferences()
                    return@appResultOf ScaffoldSummary(
                        highlights = cachedHighlights.map { entity -> entity.toDomain() },
                        lastUpdatedAtMillis = preferences.lastUpdatedAtMillis,
                        hasCompletedSetupGuide = preferences.hasSeenSetupGuide
                    )
                }

                val remoteHighlights = remoteDataSource.fetchHighlights().map { dto -> dto.toDomain() }
                if (remoteHighlights.isEmpty()) {
                    return@appResultOf ScaffoldSummary(
                        highlights = emptyList(),
                        lastUpdatedAtMillis = null,
                        hasCompletedSetupGuide = false
                    )
                }

                val updatedAtMillis = System.currentTimeMillis()
                localDataSource.replaceHighlights(
                    remoteHighlights.map { item -> item.toEntity(updatedAtMillis) }
                )
                preferencesDataSource.updateLastUpdatedAt(updatedAtMillis)
                preferencesDataSource.markSetupGuideAsSeen()
                val preferences = preferencesDataSource.getPreferences()

                ScaffoldSummary(
                    highlights = remoteHighlights,
                    lastUpdatedAtMillis = preferences.lastUpdatedAtMillis,
                    hasCompletedSetupGuide = preferences.hasSeenSetupGuide
                )
            }
        }
        return if (result is AppResult.Success && result.data.highlights.isEmpty()) {
            AppResult.Error(AppError.EmptyData)
        } else {
            result
        }
    }

    override suspend fun markSetupGuideAsSeen(): AppResult<Unit> {
        return withContext(dispatchers.io) {
            appResultOf { preferencesDataSource.markSetupGuideAsSeen() }
        }
    }

    override suspend fun updateAppThemeMode(themeMode: AppThemeMode): AppResult<Unit> {
        return withContext(dispatchers.io) {
            appResultOf { preferencesDataSource.updateAppThemeMode(themeMode) }
        }
    }
}

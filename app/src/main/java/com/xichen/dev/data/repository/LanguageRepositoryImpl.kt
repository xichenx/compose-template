package com.xichen.dev.data.repository

import com.xichen.dev.core.coroutine.AppDispatchers
import com.xichen.dev.data.common.appResultOf
import com.xichen.dev.data.local.datastore.LanguagePreferencesDataSource
import com.xichen.dev.data.localization.AppLanguageManager
import com.xichen.dev.domain.model.AppLanguage
import com.xichen.dev.domain.model.AppResult
import com.xichen.dev.domain.repository.LanguageRepository
import javax.inject.Inject
import kotlinx.coroutines.withContext

class LanguageRepositoryImpl @Inject constructor(
    private val preferencesDataSource: LanguagePreferencesDataSource,
    private val languageManager: AppLanguageManager,
    private val dispatchers: AppDispatchers
) : LanguageRepository {

    override suspend fun getAppLanguage(): AppResult<AppLanguage> {
        return withContext(dispatchers.io) {
            appResultOf { preferencesDataSource.getAppLanguage() }
        }
    }

    override suspend fun updateAppLanguage(language: AppLanguage): AppResult<Unit> {
        return appResultOf {
            withContext(dispatchers.io) {
                preferencesDataSource.updateAppLanguage(language)
            }
            languageManager.applyLanguage(language)
        }
    }

    override suspend fun initializeAppLanguage(): AppResult<Unit> {
        return appResultOf {
            val language = withContext(dispatchers.io) {
                preferencesDataSource.getAppLanguage()
            }
            languageManager.applyLanguage(language)
        }
    }
}

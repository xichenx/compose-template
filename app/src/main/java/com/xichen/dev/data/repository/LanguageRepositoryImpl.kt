package com.xichen.dev.data.repository

import com.xichen.dev.core.coroutine.AppDispatchers
import com.xichen.dev.data.local.datastore.LanguagePreferencesDataSource
import com.xichen.dev.data.localization.AppLanguageManager
import com.xichen.dev.domain.model.AppLanguage
import com.xichen.dev.domain.repository.LanguageRepository
import javax.inject.Inject
import kotlinx.coroutines.withContext

class LanguageRepositoryImpl @Inject constructor(
    private val preferencesDataSource: LanguagePreferencesDataSource,
    private val languageManager: AppLanguageManager,
    private val dispatchers: AppDispatchers
) : LanguageRepository {

    override suspend fun getAppLanguage(): AppLanguage {
        return withContext(dispatchers.io) {
            preferencesDataSource.getAppLanguage()
        }
    }

    override suspend fun updateAppLanguage(language: AppLanguage) {
        withContext(dispatchers.io) {
            preferencesDataSource.updateAppLanguage(language)
        }
        languageManager.applyLanguage(language)
    }

    override suspend fun initializeAppLanguage() {
        val language = withContext(dispatchers.io) {
            preferencesDataSource.getAppLanguage()
        }
        languageManager.applyLanguage(language)
    }
}

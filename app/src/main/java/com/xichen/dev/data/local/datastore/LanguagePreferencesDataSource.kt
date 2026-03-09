package com.xichen.dev.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.xichen.dev.domain.model.AppLanguage
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.languageDataStore by preferencesDataStore(name = "language_preferences")

class LanguagePreferencesDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun getAppLanguage(): AppLanguage {
        return read(context)
    }

    suspend fun updateAppLanguage(language: AppLanguage) {
        context.languageDataStore.edit { preferences ->
            preferences[Keys.AppLanguage] = language.storageKey
        }
    }

    private fun toAppLanguage(preferences: Preferences): AppLanguage {
        return AppLanguage.fromStorageKey(preferences[Keys.AppLanguage])
    }

    companion object {
        suspend fun read(context: Context): AppLanguage {
            return context.languageDataStore.data
                .map { preferences -> AppLanguage.fromStorageKey(preferences[Keys.AppLanguage]) }
                .first()
        }
    }

    private object Keys {
        val AppLanguage = stringPreferencesKey("app_language")
    }
}

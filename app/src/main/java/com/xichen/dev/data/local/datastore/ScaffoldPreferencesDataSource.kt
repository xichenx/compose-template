package com.xichen.dev.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.xichen.dev.domain.model.AppThemeMode
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.scaffoldDataStore by preferencesDataStore(name = "scaffold_preferences")

class ScaffoldPreferencesDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun observeAppThemeMode(): Flow<AppThemeMode> {
        return context.scaffoldDataStore.data
            .map { preferences -> AppThemeMode.fromStorageKey(preferences[Keys.AppThemeMode]) }
    }

    suspend fun getAppThemeMode(): AppThemeMode {
        return observeAppThemeMode().first()
    }

    suspend fun getPreferences(): ScaffoldPreferences {
        return context.scaffoldDataStore.data.map(::toPreferences).first()
    }

    suspend fun markSetupGuideAsSeen() {
        context.scaffoldDataStore.edit { preferences ->
            preferences[Keys.HasSeenSetupGuide] = true
        }
    }

    suspend fun updateLastUpdatedAt(timestampMillis: Long) {
        context.scaffoldDataStore.edit { preferences ->
            preferences[Keys.LastUpdatedAt] = timestampMillis
        }
    }

    suspend fun updateAppThemeMode(themeMode: AppThemeMode) {
        context.scaffoldDataStore.edit { preferences ->
            preferences[Keys.AppThemeMode] = themeMode.storageKey
        }
    }

    private fun toPreferences(preferences: Preferences): ScaffoldPreferences {
        return ScaffoldPreferences(
            hasSeenSetupGuide = preferences[Keys.HasSeenSetupGuide] ?: false,
            lastUpdatedAtMillis = preferences[Keys.LastUpdatedAt],
            appThemeMode = AppThemeMode.fromStorageKey(preferences[Keys.AppThemeMode])
        )
    }

    private object Keys {
        val HasSeenSetupGuide = booleanPreferencesKey("has_seen_setup_guide")
        val LastUpdatedAt = longPreferencesKey("last_updated_at")
        val AppThemeMode = stringPreferencesKey("app_theme_mode")
    }
}

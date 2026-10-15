package com.xichen.dev.data.local.datastore

import com.xichen.dev.domain.model.AppThemeMode

data class ScaffoldPreferences(
    val hasSeenSetupGuide: Boolean = false,
    val lastUpdatedAtMillis: Long? = null,
    val appThemeMode: AppThemeMode = AppThemeMode.default
)

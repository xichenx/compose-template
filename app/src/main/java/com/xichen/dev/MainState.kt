package com.xichen.dev

import com.xichen.dev.domain.model.AppThemeMode

data class MainState(
    val themeMode: AppThemeMode = AppThemeMode.default
)

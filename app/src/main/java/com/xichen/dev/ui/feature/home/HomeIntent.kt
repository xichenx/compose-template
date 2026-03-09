package com.xichen.dev.ui.feature.home

import com.xichen.dev.domain.model.AppLanguage
import com.xichen.dev.domain.model.AppThemeMode
import com.xichen.dev.ui.mvi.UiIntent

sealed interface HomeIntent : UiIntent {
    data object Initialize : HomeIntent
    data object RetryLoad : HomeIntent
    data object OpenLoginDemo : HomeIntent
    data object ClearStageFilters : HomeIntent
    data class SelectTab(val tab: HomeTab) : HomeIntent
    data class SelectAppLanguage(val language: AppLanguage) : HomeIntent
    data class SelectAppThemeMode(val themeMode: AppThemeMode) : HomeIntent
    data class SearchModules(val query: String) : HomeIntent
    data class ToggleStageFilter(val stage: String) : HomeIntent
    data class HighlightClick(val id: String) : HomeIntent
}

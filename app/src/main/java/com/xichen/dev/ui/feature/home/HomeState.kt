package com.xichen.dev.ui.feature.home

import com.xichen.dev.R
import com.xichen.dev.domain.model.AppLanguage
import com.xichen.dev.domain.model.AppThemeMode
import com.xichen.dev.domain.model.ScaffoldHighlight
import com.xichen.dev.ui.common.UiText
import com.xichen.dev.ui.mvi.PagedListState
import com.xichen.dev.ui.mvi.UiState

data class HomeState(
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null,
    val highlightListState: PagedListState<ScaffoldHighlight> = PagedListState(),
    val lastUpdatedText: UiText = UiText.StringResource(R.string.home_last_sync_empty),
    val setupStatusText: UiText = UiText.StringResource(R.string.home_setup_pending),
    val selectedAppLanguage: AppLanguage = AppLanguage.default,
    val selectedAppThemeMode: AppThemeMode = AppThemeMode.default,
    val selectedTab: HomeTab = HomeTab.Overview,
    val modulesSearchQuery: String = "",
    val selectedStages: Set<String> = emptySet()
) : UiState {
    val highlights: List<ScaffoldHighlight>
        get() = highlightListState.items

    val availableStages: List<String>
        get() = highlightListState.items
            .map(ScaffoldHighlight::stage)
            .distinct()
            .sorted()

    val filteredHighlights: List<ScaffoldHighlight>
        get() = highlightListState.items.filter { highlight ->
            val matchesQuery = modulesSearchQuery.isBlank() ||
                highlight.title.contains(modulesSearchQuery, ignoreCase = true) ||
                highlight.description.contains(modulesSearchQuery, ignoreCase = true) ||
                highlight.stage.contains(modulesSearchQuery, ignoreCase = true)
            val matchesStage = selectedStages.isEmpty() || highlight.stage in selectedStages
            matchesQuery && matchesStage
        }

    val hasActiveModuleFilters: Boolean
        get() = modulesSearchQuery.isNotBlank() || selectedStages.isNotEmpty()

    val isEmpty: Boolean
        get() = !isLoading && errorMessage == null && highlightListState.items.isEmpty()
}

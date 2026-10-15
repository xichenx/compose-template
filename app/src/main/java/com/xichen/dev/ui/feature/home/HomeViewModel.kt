package com.xichen.dev.ui.feature.home

import com.xichen.dev.R
import com.xichen.dev.domain.model.AppLanguage
import com.xichen.dev.domain.model.AppThemeMode
import com.xichen.dev.domain.model.ScaffoldSummary
import com.xichen.dev.domain.model.onError
import com.xichen.dev.domain.model.onSuccess
import com.xichen.dev.domain.usecase.GetScaffoldHighlightsParams
import com.xichen.dev.domain.usecase.GetAppLanguageUseCase
import com.xichen.dev.domain.usecase.GetScaffoldHighlightsUseCase
import com.xichen.dev.domain.usecase.ObserveAppThemeModeUseCase
import com.xichen.dev.domain.usecase.UpdateAppLanguageUseCase
import com.xichen.dev.domain.usecase.UpdateAppThemeModeUseCase
import com.xichen.dev.ui.common.UiText
import com.xichen.dev.ui.common.asUiText
import com.xichen.dev.ui.mvi.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.flow.collect

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getScaffoldHighlightsUseCase: GetScaffoldHighlightsUseCase,
    private val getAppLanguageUseCase: GetAppLanguageUseCase,
    private val updateAppLanguageUseCase: UpdateAppLanguageUseCase,
    private val observeAppThemeModeUseCase: ObserveAppThemeModeUseCase,
    private val updateAppThemeModeUseCase: UpdateAppThemeModeUseCase
) : BaseViewModel<HomeState, HomeEffect>(HomeState()) {
    private var hasStartedThemeObserver = false

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.Initialize -> {
                loadLanguagePreference()
                observeThemeModePreference()
                loadHighlights()
            }
            HomeIntent.RetryLoad -> loadHighlights(forceRefresh = true)
            HomeIntent.OpenLoginDemo -> sendEffect { HomeEffect.NavigateToLogin }
            HomeIntent.ClearStageFilters -> clearStageFilters()
            is HomeIntent.SelectAppLanguage -> onSelectAppLanguage(intent.language)
            is HomeIntent.SelectAppThemeMode -> onSelectAppThemeMode(intent.themeMode)
            is HomeIntent.SelectTab -> onSelectTab(intent.tab)
            is HomeIntent.SearchModules -> onSearchModules(intent.query)
            is HomeIntent.ToggleStageFilter -> onToggleStageFilter(intent.stage)
            is HomeIntent.HighlightClick -> onHighlightClick(intent.id)
        }
    }

    private fun loadHighlights(forceRefresh: Boolean = false) {
        if (currentState.isLoading || currentState.highlightListState.isRefreshing) {
            return
        }

        val hasContent = currentState.highlightListState.items.isNotEmpty()
        setState {
            copy(
                isLoading = !hasContent,
                errorMessage = null,
                highlightListState = if (hasContent) {
                    highlightListState.markRefreshing()
                } else {
                    highlightListState.stopLoading()
                }
            )
        }
        launchOn {
            getScaffoldHighlightsUseCase(GetScaffoldHighlightsParams(forceRefresh = forceRefresh))
                .onSuccess { summary ->
                    onLoadSuccess(summary, forceRefresh)
                }
                .onError { error ->
                    setState {
                        copy(
                            isLoading = false,
                            errorMessage = if (hasContent) null else error.asUiText(),
                            highlightListState = highlightListState.stopLoading()
                        )
                    }
                    if (hasContent) {
                        sendEffect { HomeEffect.ShowMessage(error.asUiText()) }
                    }
                }
        }
    }

    private fun onLoadSuccess(summary: ScaffoldSummary, forceRefresh: Boolean) {
        setState {
            copy(
                isLoading = false,
                errorMessage = null,
                highlightListState = highlightListState.replaceFirstPage(
                    newItems = summary.highlights,
                    hasMore = false
                ),
                lastUpdatedText = summary.lastUpdatedAtMillis?.let { timestamp ->
                    UiText.StringResource(
                        resId = R.string.home_last_sync_prefix,
                        args = listOf(formatTimeLabel(timestamp))
                    )
                } ?: UiText.StringResource(R.string.home_last_sync_empty),
                setupStatusText = if (summary.hasCompletedSetupGuide) {
                    UiText.StringResource(R.string.home_setup_complete)
                } else {
                    UiText.StringResource(R.string.home_setup_pending)
                }
            )
        }
        if (forceRefresh) {
            sendEffect {
                HomeEffect.ShowMessage(UiText.StringResource(R.string.message_scaffold_refreshed))
            }
        }
    }

    private fun onHighlightClick(id: String) {
        val selected = currentState.highlights.firstOrNull { it.id == id } ?: return
        sendEffect {
            HomeEffect.ShowMessage(
                UiText.StringResource(
                    resId = R.string.message_feature_ready,
                    args = listOf(selected.title)
                )
            )
        }
    }

    private fun onSelectTab(tab: HomeTab) {
        if (currentState.selectedTab == tab) return
        setState { copy(selectedTab = tab) }
    }

    private fun onSearchModules(query: String) {
        if (currentState.modulesSearchQuery == query) return
        setState { copy(modulesSearchQuery = query) }
    }

    private fun onToggleStageFilter(stage: String) {
        setState {
            val nextStages = selectedStages.toMutableSet().apply {
                if (!add(stage)) {
                    remove(stage)
                }
            }
            copy(selectedStages = nextStages)
        }
    }

    private fun clearStageFilters() {
        if (currentState.selectedStages.isEmpty()) return
        setState { copy(selectedStages = emptySet()) }
    }

    private fun loadLanguagePreference() {
        launchOn {
            getAppLanguageUseCase().onSuccess { language ->
                setState { copy(selectedAppLanguage = language) }
            }.onError { error ->
                sendEffect { HomeEffect.ShowMessage(error.asUiText()) }
            }
        }
    }

    private fun onSelectAppLanguage(language: AppLanguage) {
        if (currentState.selectedAppLanguage == language) return

        launchOn {
            updateAppLanguageUseCase(language).onSuccess {
                setState { copy(selectedAppLanguage = language) }
                sendEffect { HomeEffect.RecreateActivity }
            }.onError { error ->
                sendEffect { HomeEffect.ShowMessage(error.asUiText()) }
            }
        }
    }

    private fun observeThemeModePreference() {
        if (hasStartedThemeObserver) return
        hasStartedThemeObserver = true

        launchOn {
            observeAppThemeModeUseCase().collect { themeMode ->
                setState { copy(selectedAppThemeMode = themeMode) }
            }
        }
    }

    private fun onSelectAppThemeMode(themeMode: AppThemeMode) {
        if (currentState.selectedAppThemeMode == themeMode) return

        launchOn {
            updateAppThemeModeUseCase(themeMode).onSuccess {
                setState { copy(selectedAppThemeMode = themeMode) }
            }.onError { error ->
                sendEffect { HomeEffect.ShowMessage(error.asUiText()) }
            }
        }
    }

    private fun formatTimeLabel(timestampMillis: Long): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return formatter.format(Date(timestampMillis))
    }
}

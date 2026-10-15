package com.xichen.dev.ui.feature.home

import android.app.Activity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xichen.dev.R
import com.xichen.dev.domain.model.AppLanguage
import com.xichen.dev.domain.model.AppThemeMode
import com.xichen.dev.ui.common.AppActionCard
import com.xichen.dev.ui.common.AppFilterChipOption
import com.xichen.dev.ui.common.AppFilterChipRow
import com.xichen.dev.ui.common.AppInfoCard
import com.xichen.dev.ui.common.AppListContainer
import com.xichen.dev.ui.common.AppListHeader
import com.xichen.dev.ui.common.AppPagingFooter
import com.xichen.dev.ui.common.AppPageScaffold
import com.xichen.dev.ui.common.AppSection
import com.xichen.dev.ui.common.AppSearchBar
import com.xichen.dev.ui.common.AppStateContent
import com.xichen.dev.ui.common.AppStatusCard
import com.xichen.dev.ui.common.AppTopBar
import com.xichen.dev.domain.model.ScaffoldHighlight
import com.xichen.dev.ui.common.EmptyContent
import com.xichen.dev.ui.common.UiText
import com.xichen.dev.ui.common.asString
import com.xichen.dev.ui.common.resolve
import com.xichen.dev.ui.feature.home.component.HomeNavigationRail
import com.xichen.dev.ui.feature.home.component.CustomHomeBottomBar
import com.xichen.dev.ui.feature.home.component.HighlightCard
import com.xichen.dev.ui.feature.home.component.HomePermanentDrawerContent
import com.xichen.dev.ui.theme.ComposetemplateTheme
import com.xichen.dev.ui.theme.appElevation
import com.xichen.dev.ui.theme.appSpacing

@Composable
fun HomeRoute(
    windowSizeClass: WindowSizeClass,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.onIntent(HomeIntent.Initialize)
    }

    LaunchedEffect(viewModel, context) {
        viewModel.effect.collect { effect ->
            when (effect) {
                HomeEffect.NavigateToLogin -> onNavigateToLogin()
                HomeEffect.RecreateActivity -> (context as? Activity)?.recreate()
                is HomeEffect.ShowMessage -> snackbarHostState.showSnackbar(effect.message.resolve(context))
            }
        }
    }

    HomeScreen(
        state = state,
        onIntent = viewModel::onIntent,
        snackbarHostState = snackbarHostState,
        windowSizeClass = windowSizeClass,
        modifier = modifier
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeScreen(
    state: HomeState,
    onIntent: (HomeIntent) -> Unit,
    snackbarHostState: SnackbarHostState,
    windowSizeClass: WindowSizeClass,
    availableTabs: List<HomeTab> = HomeTab.entries,
    modifier: Modifier = Modifier
) {
    val tabs = availableTabs.ifEmpty { listOf(HomeTab.Overview) }.distinct()
    val selectedTab = tabs.firstOrNull { it == state.selectedTab } ?: tabs.first()
    val navigationType = windowSizeClass.widthSizeClass.toHomeNavigationType()
    val spacing = MaterialTheme.appSpacing

    when {
        tabs.size <= 1 -> {
            HomeContentScaffold(
                state = state,
                selectedTab = selectedTab,
                availableTabs = tabs,
                onIntent = onIntent,
                snackbarHostState = snackbarHostState,
                modifier = modifier
            )
        }

        navigationType == HomeNavigationType.BottomBar -> {
            HomeContentScaffold(
                state = state,
                selectedTab = selectedTab,
                availableTabs = tabs,
                onIntent = onIntent,
                snackbarHostState = snackbarHostState,
                modifier = modifier,
                bottomBar = {
                    CustomHomeBottomBar(
                        tabs = tabs,
                        selectedTab = selectedTab,
                        onTabSelected = { onIntent(HomeIntent.SelectTab(it)) },
                        modifier = Modifier.padding(horizontal = spacing.lg, vertical = spacing.sm)
                    )
                }
            )
        }

        navigationType == HomeNavigationType.NavigationRail -> {
            HomeContentScaffold(
                state = state,
                selectedTab = selectedTab,
                availableTabs = tabs,
                onIntent = onIntent,
                snackbarHostState = snackbarHostState,
                modifier = modifier
            ) { innerPadding ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    HomeNavigationRail(
                        tabs = tabs,
                        selectedTab = selectedTab,
                        onTabSelected = { onIntent(HomeIntent.SelectTab(it)) }
                    )
                    HomeTabContent(
                        selectedTab = selectedTab,
                        availableTabs = tabs,
                        state = state,
                        onIntent = onIntent,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        else -> {
            PermanentNavigationDrawer(
                drawerContent = {
                    HomePermanentDrawerContent(
                        tabs = tabs,
                        selectedTab = selectedTab,
                        onTabSelected = { onIntent(HomeIntent.SelectTab(it)) }
                    )
                }
            ) {
                HomeContentScaffold(
                    state = state,
                    selectedTab = selectedTab,
                    availableTabs = tabs,
                    onIntent = onIntent,
                    snackbarHostState = snackbarHostState,
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun HomeContentScaffold(
    state: HomeState,
    selectedTab: HomeTab,
    availableTabs: List<HomeTab>,
    onIntent: (HomeIntent) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    bottomBar: @Composable (() -> Unit)? = null,
    content: @Composable ((PaddingValues) -> Unit)? = null
) {
    AppPageScaffold(
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        topBar = {
            AppTopBar(
                title = stringResource(selectedTab.titleRes)
            )
        },
        bottomBar = {
            bottomBar?.invoke()
        }
    ) { innerPadding ->
        if (content != null) {
            content(innerPadding)
        } else {
            HomeTabContent(
                selectedTab = selectedTab,
                availableTabs = availableTabs,
                state = state,
                onIntent = onIntent,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
private fun HomeTabContent(
    selectedTab: HomeTab,
    availableTabs: List<HomeTab>,
    state: HomeState,
    onIntent: (HomeIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        targetState = selectedTab,
        modifier = modifier,
        transitionSpec = {
            tabContentTransition(initialState = initialState, targetState = targetState)
        },
        label = "homeTabContent"
    ) { tab ->
        when (tab) {
            HomeTab.Overview -> AppStateContent(
                isLoading = state.isLoading,
                errorMessage = state.errorMessage,
                isEmpty = state.isEmpty,
                modifier = Modifier.fillMaxSize(),
                loadingMessage = UiText.StringResource(R.string.home_loading),
                emptyTitle = UiText.StringResource(R.string.home_empty_title),
                onRetry = { onIntent(HomeIntent.RetryLoad) }
            ) {
                HomeOverviewContent(
                    state = state,
                    hasModulesTab = HomeTab.Modules in availableTabs,
                    onIntent = onIntent,
                    modifier = Modifier.fillMaxSize()
                )
            }

            HomeTab.Modules -> AppStateContent(
                isLoading = state.isLoading,
                errorMessage = state.errorMessage,
                isEmpty = false,
                modifier = Modifier.fillMaxSize(),
                loadingMessage = UiText.StringResource(R.string.home_loading),
                onRetry = { onIntent(HomeIntent.RetryLoad) }
            ) {
                HomeModulesContent(
                    searchQuery = state.modulesSearchQuery,
                    stageOptions = state.availableStages,
                    selectedStages = state.selectedStages,
                    highlights = state.filteredHighlights,
                    hasActiveFilters = state.hasActiveModuleFilters,
                    isLoadingMore = state.highlightListState.isLoadingMore,
                    hasMore = state.highlightListState.hasMore,
                    onSearchQueryChange = { onIntent(HomeIntent.SearchModules(it)) },
                    onClearStageFilters = { onIntent(HomeIntent.ClearStageFilters) },
                    onStageClick = { onIntent(HomeIntent.ToggleStageFilter(it)) },
                    onHighlightClick = { onIntent(HomeIntent.HighlightClick(it)) },
                    modifier = Modifier.fillMaxSize()
                )
            }

            HomeTab.Profile -> HomeProfileContent(
                state = state,
                onOpenLoginDemo = { onIntent(HomeIntent.OpenLoginDemo) },
                onSelectAppLanguage = { onIntent(HomeIntent.SelectAppLanguage(it)) },
                onSelectAppThemeMode = { onIntent(HomeIntent.SelectAppThemeMode(it)) },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

private fun tabContentTransition(
    initialState: HomeTab,
    targetState: HomeTab
): ContentTransform {
    val goingForward = targetState.ordinal > initialState.ordinal
    val duration = 280

    return (
        slideInHorizontally(
            initialOffsetX = { fullWidth ->
                if (goingForward) fullWidth / 5 else -(fullWidth / 5)
            },
            animationSpec = tween(durationMillis = duration)
        ) + fadeIn(animationSpec = tween(durationMillis = 220))
    ).togetherWith(
        slideOutHorizontally(
            targetOffsetX = { fullWidth ->
                if (goingForward) -(fullWidth / 8) else fullWidth / 8
            },
            animationSpec = tween(durationMillis = duration)
        ) + fadeOut(animationSpec = tween(durationMillis = 180))
    )
}

@Composable
private fun HomeOverviewContent(
    state: HomeState,
    hasModulesTab: Boolean,
    onIntent: (HomeIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    AppListContainer(modifier = modifier.fillMaxSize()) {
        item {
            AppSection(
                title = stringResource(R.string.home_tab_overview),
                description = stringResource(R.string.home_subtitle)
            ) {
                Text(
                    text = stringResource(R.string.home_intro),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        item {
            HomeSummaryCard(
                setupStatus = state.setupStatusText,
                lastUpdatedText = state.lastUpdatedText,
                isRefreshing = state.highlightListState.isRefreshing,
                onPrimaryAction = { onIntent(HomeIntent.OpenLoginDemo) },
                primaryActionText = stringResource(R.string.home_open_login_demo)
            )
        }
        if (hasModulesTab) {
            item {
                HomeActionCard(
                    title = stringResource(R.string.home_recent_modules_title),
                    description = stringResource(R.string.home_recent_modules_subtitle),
                    primaryActionText = stringResource(R.string.home_open_modules_tab),
                    onPrimaryAction = { onIntent(HomeIntent.SelectTab(HomeTab.Modules)) },
                    secondaryActionText = stringResource(R.string.home_refresh_data),
                    onSecondaryAction = { onIntent(HomeIntent.RetryLoad) }
                )
            }
        }
        items(
            items = state.highlights.take(2),
            key = { item -> item.id }
        ) { item ->
            HighlightCard(
                item = item,
                onClick = { onIntent(HomeIntent.HighlightClick(it)) }
            )
        }
    }
}

@Composable
private fun HomeModulesContent(
    searchQuery: String,
    stageOptions: List<String>,
    selectedStages: Set<String>,
    highlights: List<ScaffoldHighlight>,
    hasActiveFilters: Boolean,
    isLoadingMore: Boolean,
    hasMore: Boolean,
    onSearchQueryChange: (String) -> Unit,
    onClearStageFilters: () -> Unit,
    onStageClick: (String) -> Unit,
    onHighlightClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val filterOptions = buildList {
        add(
            AppFilterChipOption(
                id = "all",
                label = UiText.StringResource(R.string.home_modules_filter_all)
            )
        )
        addAll(
            stageOptions.map { stage ->
                AppFilterChipOption(
                    id = stage,
                    label = UiText.DynamicString(stage)
                )
            }
        )
    }

    AppListContainer(
        modifier = modifier.fillMaxSize(),
        verticalItemSpacing = MaterialTheme.appSpacing.md
    ) {
        item {
            AppListHeader(
                title = UiText.StringResource(R.string.home_tab_modules),
                description = UiText.StringResource(R.string.home_recent_modules_subtitle),
                supportingText = UiText.StringResource(
                    R.string.home_modules_result_count,
                    args = listOf(highlights.size)
                )
            )
        }
        item {
            AppSearchBar(
                query = searchQuery,
                onQueryChange = onSearchQueryChange,
                placeholder = UiText.StringResource(R.string.home_modules_search_placeholder)
            )
        }
        if (filterOptions.size > 1) {
            item {
                AppFilterChipRow(
                    options = filterOptions,
                    selectedOptionIds = if (selectedStages.isEmpty()) {
                        setOf("all")
                    } else {
                        selectedStages
                    },
                    onOptionClick = { option ->
                        if (option.id == "all") {
                            onClearStageFilters()
                        } else {
                            onStageClick(option.id)
                        }
                    },
                    contentPadding = PaddingValues(horizontal = 0.dp)
                )
            }
        }
        if (highlights.isEmpty()) {
            item {
                EmptyContent(
                    title = UiText.StringResource(
                        if (hasActiveFilters) R.string.home_modules_empty_filtered_title else R.string.home_empty_title
                    ),
                    description = if (hasActiveFilters) {
                        UiText.StringResource(R.string.home_modules_empty_filtered_description)
                    } else {
                        null
                    },
                    fullscreen = false
                )
            }
        }
        items(
            items = highlights,
            key = { item -> item.id }
        ) { item ->
            HighlightCard(
                item = item,
                onClick = onHighlightClick
            )
        }
        if (highlights.isNotEmpty()) {
            item {
                AppPagingFooter(
                    isLoadingMore = isLoadingMore,
                    hasMore = hasMore
                )
            }
        }
    }
}

@Composable
private fun HomeProfileContent(
    state: HomeState,
    onOpenLoginDemo: () -> Unit,
    onSelectAppLanguage: (AppLanguage) -> Unit,
    onSelectAppThemeMode: (AppThemeMode) -> Unit,
    modifier: Modifier = Modifier
) {
    AppListContainer(modifier = modifier.fillMaxSize()) {
        item {
            AppSection(
                title = stringResource(R.string.home_profile_title),
                description = stringResource(R.string.home_profile_subtitle)
            )
        }
        item {
            HomeSummaryCard(
                setupStatus = state.setupStatusText,
                lastUpdatedText = state.lastUpdatedText,
                isRefreshing = state.highlightListState.isRefreshing,
                onPrimaryAction = onOpenLoginDemo,
                primaryActionText = stringResource(R.string.home_open_login_demo)
            )
        }
        item {
            AppSection(
                title = stringResource(R.string.home_language_title),
                description = stringResource(R.string.home_language_description)
            ) {
                AppInfoCard(
                    title = stringResource(R.string.home_language_current_title),
                    description = stringResource(
                        R.string.home_language_current_description,
                        stringResource(state.selectedAppLanguage.labelRes)
                    )
                )
                AppFilterChipRow(
                    options = AppLanguage.entries.map { language ->
                        AppFilterChipOption(
                            id = language.storageKey,
                            label = UiText.StringResource(language.labelRes)
                        )
                    },
                    selectedOptionIds = setOf(state.selectedAppLanguage.storageKey),
                    onOptionClick = { option ->
                        AppLanguage.entries
                            .firstOrNull { language -> language.storageKey == option.id }
                            ?.let(onSelectAppLanguage)
                    },
                    contentPadding = PaddingValues(horizontal = 0.dp)
                )
            }
        }
        item {
            AppSection(
                title = stringResource(R.string.home_theme_title),
                description = stringResource(R.string.home_theme_description)
            ) {
                AppInfoCard(
                    title = stringResource(R.string.home_theme_current_title),
                    description = stringResource(
                        R.string.home_theme_current_description,
                        stringResource(state.selectedAppThemeMode.labelRes)
                    )
                )
                AppFilterChipRow(
                    options = AppThemeMode.entries.map { themeMode ->
                        AppFilterChipOption(
                            id = themeMode.storageKey,
                            label = UiText.StringResource(themeMode.labelRes)
                        )
                    },
                    selectedOptionIds = setOf(state.selectedAppThemeMode.storageKey),
                    onOptionClick = { option ->
                        AppThemeMode.entries
                            .firstOrNull { themeMode -> themeMode.storageKey == option.id }
                            ?.let(onSelectAppThemeMode)
                    },
                    contentPadding = PaddingValues(horizontal = 0.dp)
                )
            }
        }
        item {
            AppInfoCard(
                title = stringResource(R.string.home_profile_status_title),
                description = stringResource(R.string.home_profile_status_body)
            )
        }
        item {
            HomeActionCard(
                title = stringResource(R.string.home_profile_action_title),
                description = stringResource(R.string.home_profile_action_body),
                primaryActionText = stringResource(R.string.home_open_login_demo),
                onPrimaryAction = onOpenLoginDemo
            )
        }
    }
}

@Composable
private fun HomeSummaryCard(
    setupStatus: UiText,
    lastUpdatedText: UiText,
    isRefreshing: Boolean,
    onPrimaryAction: () -> Unit,
    primaryActionText: String,
    modifier: Modifier = Modifier
) {
    AppStatusCard(
        label = UiText.StringResource(R.string.home_stage_title),
        title = setupStatus,
        description = lastUpdatedText,
        supportingText = if (isRefreshing) {
            UiText.StringResource(R.string.home_refreshing)
        } else {
            null
        },
        actionText = UiText.DynamicString(primaryActionText),
        onActionClick = onPrimaryAction,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    )
}

private val AppLanguage.labelRes: Int
    get() = when (this) {
        AppLanguage.FollowSystem -> R.string.home_language_follow_system
        AppLanguage.English -> R.string.home_language_english
        AppLanguage.SimplifiedChinese -> R.string.home_language_simplified_chinese
    }

private val AppThemeMode.labelRes: Int
    get() = when (this) {
        AppThemeMode.FollowSystem -> R.string.home_theme_follow_system
        AppThemeMode.Light -> R.string.home_theme_light
        AppThemeMode.Dark -> R.string.home_theme_dark
    }

@Composable
private fun HomeActionCard(
    title: String,
    description: String,
    primaryActionText: String,
    onPrimaryAction: () -> Unit,
    secondaryActionText: String? = null,
    onSecondaryAction: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    AppActionCard(
        title = UiText.DynamicString(title),
        description = UiText.DynamicString(description),
        primaryActionText = UiText.DynamicString(primaryActionText),
        onPrimaryAction = onPrimaryAction,
        modifier = modifier,
        secondaryActionText = secondaryActionText?.let(UiText::DynamicString),
        onSecondaryAction = onSecondaryAction
    )
}

@Preview(showBackground = true)
@Composable
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
private fun HomeScreenPreview() {
    ComposetemplateTheme {
        HomeScreen(
            state = HomeState(
                highlightListState = com.xichen.dev.ui.mvi.PagedListState(
                    items = listOf(
                        ScaffoldHighlight(
                            id = "preview-1",
                            title = "模块结构",
                            description = "Screen、ViewModel、State、Intent、Effect 已形成固定模板。",
                            stage = "UI 层"
                        ),
                        ScaffoldHighlight(
                            id = "preview-2",
                            title = "依赖治理",
                            description = "版本统一放在 libs.versions.toml 中集中管理。",
                            stage = "工程层"
                        )
                    )
                ),
                lastUpdatedText = UiText.StringResource(
                    resId = R.string.home_last_sync_prefix,
                    args = listOf("2026-03-06 10:00")
                ),
                setupStatusText = UiText.StringResource(R.string.home_setup_complete)
            ),
            onIntent = {},
            snackbarHostState = remember { SnackbarHostState() },
            windowSizeClass = androidx.compose.material3.windowsizeclass.WindowSizeClass.calculateFromSize(
                androidx.compose.ui.unit.DpSize(412.dp, 892.dp)
            ),
            availableTabs = HomeTab.entries
        )
    }
}

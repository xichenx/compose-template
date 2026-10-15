package com.xichen.dev.ui.feature.home

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.xichen.dev.R
import com.xichen.dev.domain.model.ScaffoldHighlight
import com.xichen.dev.ui.common.UiText
import com.xichen.dev.ui.mvi.PagedListState
import com.xichen.dev.ui.theme.ComposetemplateTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class HomeScreenTest {
    @get:Rule val composeRule = createComposeRule()
    private val context get() = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun searchWithoutMatchesKeepsControlsAndCanBeCleared() {
        composeRule.setContent {
            var state by remember {
                mutableStateOf(
                    HomeState(
                        selectedTab = HomeTab.Modules,
                        highlightListState = PagedListState(
                            items = listOf(ScaffoldHighlight("mvi", "MVI", "State management", "UI")),
                            hasMore = false
                        )
                    )
                )
            }
            ComposetemplateTheme(dynamicColor = false) {
                HomeScreen(
                    state = state,
                    onIntent = { intent ->
                        state = when (intent) {
                            is HomeIntent.SearchModules -> state.copy(modulesSearchQuery = intent.query)
                            is HomeIntent.ToggleStageFilter -> state.copy(selectedStages = setOf(intent.stage))
                            HomeIntent.ClearStageFilters -> state.copy(selectedStages = emptySet())
                            else -> state
                        }
                    },
                    snackbarHostState = remember { SnackbarHostState() },
                    windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(412.dp, 892.dp)),
                    availableTabs = listOf(HomeTab.Modules)
                )
            }
        }
        composeRule.onNode(hasSetTextAction()).performTextInput("no-match")
        composeRule.onNodeWithText(context.getString(R.string.home_modules_empty_filtered_title)).assertExists()
        composeRule.onNode(hasSetTextAction()).assertExists()
        composeRule.onNodeWithText(context.getString(R.string.home_modules_filter_all)).assertExists()
        composeRule.onNodeWithContentDescription(context.getString(R.string.common_clear_search)).performClick()
        composeRule.onNodeWithText("MVI").assertExists()
        composeRule.onNodeWithText(context.getString(R.string.home_modules_empty_filtered_title)).assertDoesNotExist()
    }

    @Test
    fun loadingFailureShowsRetryAction() {
        var retries = 0
        composeRule.setContent {
            ComposetemplateTheme(dynamicColor = false) {
                HomeScreen(
                    state = HomeState(errorMessage = UiText.DynamicString("offline")),
                    onIntent = { if (it == HomeIntent.RetryLoad) retries++ },
                    snackbarHostState = remember { SnackbarHostState() },
                    windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(412.dp, 892.dp)),
                    availableTabs = listOf(HomeTab.Overview)
                )
            }
        }
        composeRule.onNodeWithText("offline").assertIsDisplayed()
        composeRule.onNodeWithText(context.getString(R.string.common_retry)).performClick()
        composeRule.runOnIdle { org.junit.Assert.assertEquals(1, retries) }
    }
}

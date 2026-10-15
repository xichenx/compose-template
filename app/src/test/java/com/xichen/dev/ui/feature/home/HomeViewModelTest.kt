package com.xichen.dev.ui.feature.home

import com.xichen.dev.domain.model.AppError
import com.xichen.dev.domain.model.AppLanguage
import com.xichen.dev.domain.model.AppResult
import com.xichen.dev.domain.model.AppThemeMode
import com.xichen.dev.domain.usecase.GetAppLanguageUseCase
import com.xichen.dev.domain.usecase.GetScaffoldHighlightsUseCase
import com.xichen.dev.domain.usecase.ObserveAppThemeModeUseCase
import com.xichen.dev.domain.usecase.UpdateAppLanguageUseCase
import com.xichen.dev.domain.usecase.UpdateAppThemeModeUseCase
import com.xichen.dev.testutil.FakeLanguageRepository
import com.xichen.dev.testutil.FakeScaffoldRepository
import com.xichen.dev.testutil.MainDispatcherRule
import com.xichen.dev.testutil.testHighlights
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()
    private val scaffold = FakeScaffoldRepository()
    private val language = FakeLanguageRepository()

    private fun viewModel() = HomeViewModel(
        GetScaffoldHighlightsUseCase(scaffold),
        GetAppLanguageUseCase(language),
        UpdateAppLanguageUseCase(language),
        ObserveAppThemeModeUseCase(scaffold),
        UpdateAppThemeModeUseCase(scaffold)
    )

    @Test
    fun initializeLoadsContentAndSuppressesDuplicateRequests() = runTest {
        val vm = viewModel()
        vm.onIntent(HomeIntent.Initialize)
        vm.onIntent(HomeIntent.Initialize)
        assertTrue(vm.state.value.isLoading)
        advanceUntilIdle()
        assertFalse(vm.state.value.isLoading)
        assertEquals(testHighlights, vm.state.value.highlights)
        assertEquals(listOf(false), scaffold.refreshRequests)
    }

    @Test
    fun initialFailureCanBeRetried() = runTest {
        scaffold.summaryResult = AppResult.Error(AppError.Network)
        val vm = viewModel()
        vm.onIntent(HomeIntent.Initialize)
        advanceUntilIdle()
        assertNotNull(vm.state.value.errorMessage)
        vm.onIntent(HomeIntent.RetryLoad)
        assertTrue(vm.state.value.isLoading)
        advanceUntilIdle()
        assertEquals(listOf(false, true), scaffold.refreshRequests)
    }

    @Test
    fun refreshFailureKeepsExistingContentAndSendsFeedback() = runTest {
        val vm = viewModel()
        vm.onIntent(HomeIntent.Initialize)
        advanceUntilIdle()
        scaffold.summaryResult = AppResult.Error(AppError.Network)
        vm.onIntent(HomeIntent.RetryLoad)
        assertTrue(vm.state.value.highlightListState.isRefreshing)
        advanceUntilIdle()
        assertEquals(testHighlights, vm.state.value.highlights)
        assertNull(vm.state.value.errorMessage)
        assertFalse(vm.state.value.highlightListState.isRefreshing)
        assertTrue(vm.effect.first() is HomeEffect.ShowMessage)
    }

    @Test
    fun searchAndStageFiltersCanBeCleared() = runTest {
        val vm = viewModel()
        vm.onIntent(HomeIntent.Initialize)
        advanceUntilIdle()
        vm.onIntent(HomeIntent.SearchModules("missing"))
        assertTrue(vm.state.value.filteredHighlights.isEmpty())
        vm.onIntent(HomeIntent.SearchModules(""))
        vm.onIntent(HomeIntent.ToggleStageFilter("Data"))
        assertEquals(listOf(testHighlights[1]), vm.state.value.filteredHighlights)
        vm.onIntent(HomeIntent.ClearStageFilters)
        assertEquals(testHighlights, vm.state.value.filteredHighlights)
    }

    @Test
    fun failedLanguageUpdateDoesNotRecreateActivity() = runTest {
        language.updateResult = AppResult.Error(AppError.Local("write failed"))
        val vm = viewModel()
        vm.onIntent(HomeIntent.SelectAppLanguage(AppLanguage.SimplifiedChinese))
        advanceUntilIdle()
        assertEquals(AppLanguage.default, vm.state.value.selectedAppLanguage)
        assertTrue(vm.effect.first() is HomeEffect.ShowMessage)
    }

    @Test
    fun failedThemeUpdateKeepsPreferenceAndSendsFeedback() = runTest {
        scaffold.updateResult = AppResult.Error(AppError.Local("write failed"))
        val vm = viewModel()
        vm.onIntent(HomeIntent.SelectAppThemeMode(AppThemeMode.Dark))
        advanceUntilIdle()
        assertEquals(AppThemeMode.default, vm.state.value.selectedAppThemeMode)
        assertTrue(vm.effect.first() is HomeEffect.ShowMessage)
    }
}

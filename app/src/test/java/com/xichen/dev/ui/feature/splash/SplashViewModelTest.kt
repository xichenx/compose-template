package com.xichen.dev.ui.feature.splash

import com.xichen.dev.domain.model.AppError
import com.xichen.dev.domain.model.AppResult
import com.xichen.dev.domain.usecase.GetCachedSessionUseCase
import com.xichen.dev.testutil.FakeAuthRepository
import com.xichen.dev.testutil.MainDispatcherRule
import com.xichen.dev.testutil.testSession
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()
    private val repository = FakeAuthRepository()

    @Test
    fun missingSessionOpensAuthEntryOnlyOnce() = runTest {
        val vm = SplashViewModel(GetCachedSessionUseCase(repository))
        vm.onIntent(SplashIntent.CheckSession)
        vm.onIntent(SplashIntent.CheckSession)
        advanceUntilIdle()
        assertEquals(SplashEffect.NavigateToLogin, vm.effect.first())
        assertFalse(vm.state.value.isLoading)
        vm.onIntent(SplashIntent.CheckSession)
        advanceUntilIdle()
        assertEquals(1, repository.sessionCalls)
    }

    @Test
    fun cachedSessionOpensHome() = runTest {
        repository.sessionResult = AppResult.Success(testSession)
        val vm = SplashViewModel(GetCachedSessionUseCase(repository))
        vm.onIntent(SplashIntent.CheckSession)
        advanceUntilIdle()
        assertEquals(SplashEffect.NavigateToHome, vm.effect.first())
    }

    @Test
    fun storageFailureShowsErrorAndRetryCanRecover() = runTest {
        repository.sessionResult = AppResult.Error(AppError.Local("read failed"))
        val vm = SplashViewModel(GetCachedSessionUseCase(repository))
        vm.onIntent(SplashIntent.CheckSession)
        advanceUntilIdle()
        assertFalse(vm.state.value.isLoading)
        assertNotNull(vm.state.value.errorMessage)
        repository.sessionResult = AppResult.Success(null)
        vm.onIntent(SplashIntent.CheckSession)
        assertNull(vm.state.value.errorMessage)
        assertTrue(vm.state.value.isLoading)
        advanceUntilIdle()
        assertEquals(SplashEffect.NavigateToLogin, vm.effect.first())
        assertEquals(2, repository.sessionCalls)
    }
}

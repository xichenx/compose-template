package com.xichen.dev.ui.feature.login

import com.xichen.dev.domain.model.AppError
import com.xichen.dev.domain.model.AppResult
import com.xichen.dev.domain.usecase.LoginUseCase
import com.xichen.dev.domain.validation.AuthValidator
import com.xichen.dev.testutil.FakeAuthRepository
import com.xichen.dev.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()
    private val repository = FakeAuthRepository()

    @Test
    fun invalidEmailDoesNotCallRepository() = runTest {
        val vm = LoginViewModel(LoginUseCase(repository), AuthValidator())
        vm.onIntent(LoginIntent.EmailChanged("invalid"))
        vm.onIntent(LoginIntent.Submit)
        advanceUntilIdle()
        assertNotNull(vm.state.value.emailError)
        assertEquals(0, repository.loginCalls)
    }

    @Test
    fun successfulLoginShowsSuccessAndStopsSubmitting() = runTest {
        val vm = LoginViewModel(LoginUseCase(repository), AuthValidator())
        vm.onIntent(LoginIntent.Submit)
        advanceUntilIdle()
        assertNotNull(vm.state.value.successMessage)
        assertFalse(vm.state.value.isSubmitting)
        assertNull(vm.state.value.submitError)
    }

    @Test
    fun failedLoginCanBeSubmittedAgain() = runTest {
        repository.loginResult = AppResult.Error(AppError.Unauthorized)
        val vm = LoginViewModel(LoginUseCase(repository), AuthValidator())
        vm.onIntent(LoginIntent.Submit)
        advanceUntilIdle()
        assertNotNull(vm.state.value.submitError)
        assertFalse(vm.state.value.isSubmitting)
        vm.onIntent(LoginIntent.Submit)
        advanceUntilIdle()
        assertEquals(2, repository.loginCalls)
    }
}

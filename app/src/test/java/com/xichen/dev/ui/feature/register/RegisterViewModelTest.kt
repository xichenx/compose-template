package com.xichen.dev.ui.feature.register

import com.xichen.dev.domain.model.AppError
import com.xichen.dev.domain.model.AppResult
import com.xichen.dev.domain.usecase.RegisterUseCase
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
class RegisterViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()
    private val repository = FakeAuthRepository()

    @Test
    fun mismatchedPasswordsDoNotCallRepository() = runTest {
        val vm = RegisterViewModel(RegisterUseCase(repository), AuthValidator())
        vm.onIntent(RegisterIntent.ConfirmPasswordChanged("different"))
        vm.onIntent(RegisterIntent.Submit)
        advanceUntilIdle()
        assertNotNull(vm.state.value.confirmPasswordError)
        assertEquals(0, repository.registerCalls)
    }

    @Test
    fun successfulRegistrationShowsSuccess() = runTest {
        val vm = RegisterViewModel(RegisterUseCase(repository), AuthValidator())
        vm.onIntent(RegisterIntent.Submit)
        advanceUntilIdle()
        assertNotNull(vm.state.value.successMessage)
        assertFalse(vm.state.value.isSubmitting)
    }

    @Test
    fun registrationFailureStopsSubmitting() = runTest {
        repository.registerResult = AppResult.Error(AppError.Network)
        val vm = RegisterViewModel(RegisterUseCase(repository), AuthValidator())
        vm.onIntent(RegisterIntent.Submit)
        advanceUntilIdle()
        assertNotNull(vm.state.value.submitError)
        assertNull(vm.state.value.successMessage)
        assertFalse(vm.state.value.isSubmitting)
    }
}

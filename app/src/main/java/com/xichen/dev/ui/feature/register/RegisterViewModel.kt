package com.xichen.dev.ui.feature.register

import com.xichen.dev.R
import com.xichen.dev.domain.model.onError
import com.xichen.dev.domain.model.onSuccess
import com.xichen.dev.domain.usecase.RegisterParams
import com.xichen.dev.domain.usecase.RegisterUseCase
import com.xichen.dev.domain.validation.AuthValidator
import com.xichen.dev.domain.validation.ValidationResult
import com.xichen.dev.ui.common.UiText
import com.xichen.dev.ui.common.asUiText
import com.xichen.dev.ui.mvi.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val authValidator: AuthValidator
) : BaseViewModel<RegisterState, RegisterEffect>(RegisterState()) {

    fun onIntent(intent: RegisterIntent) {
        when (intent) {
            is RegisterIntent.DisplayNameChanged -> updateDisplayName(intent.value)
            is RegisterIntent.EmailChanged -> updateEmail(intent.value)
            is RegisterIntent.PasswordChanged -> updatePassword(intent.value)
            is RegisterIntent.ConfirmPasswordChanged -> updateConfirmPassword(intent.value)
            RegisterIntent.TogglePasswordVisibility -> togglePasswordVisibility()
            RegisterIntent.ToggleConfirmPasswordVisibility -> toggleConfirmPasswordVisibility()
            RegisterIntent.Submit -> submit()
            RegisterIntent.DismissSuccess -> clearSuccessState()
            RegisterIntent.BackClick -> sendEffect { RegisterEffect.NavigateBack }
        }
    }

    private fun updateDisplayName(value: String) {
        setState {
            copy(
                displayName = value,
                displayNameError = null,
                submitError = null,
                successMessage = null
            )
        }
    }

    private fun updateEmail(value: String) {
        setState {
            copy(
                email = value,
                emailError = null,
                submitError = null,
                successMessage = null
            )
        }
    }

    private fun updatePassword(value: String) {
        setState {
            copy(
                password = value,
                passwordError = null,
                confirmPasswordError = null,
                submitError = null,
                successMessage = null
            )
        }
    }

    private fun updateConfirmPassword(value: String) {
        setState {
            copy(
                confirmPassword = value,
                confirmPasswordError = null,
                submitError = null,
                successMessage = null
            )
        }
    }

    private fun togglePasswordVisibility() {
        setState {
            copy(isPasswordVisible = !isPasswordVisible)
        }
    }

    private fun toggleConfirmPasswordVisibility() {
        setState {
            copy(isConfirmPasswordVisible = !isConfirmPasswordVisible)
        }
    }

    private fun submit() {
        val displayName = currentState.displayName.trim()
        val email = currentState.email.trim()
        val password = currentState.password
        val confirmPassword = currentState.confirmPassword

        val displayNameError = if (displayName.isBlank()) {
            UiText.StringResource(R.string.register_name_required)
        } else {
            null
        }
        val emailError = authValidator.validateEmail(email).toUiTextOrNull()
        val passwordError = authValidator.validatePassword(password).toUiTextOrNull()
        val confirmPasswordError = when {
            confirmPassword.isBlank() -> UiText.StringResource(R.string.register_confirm_password_required)
            confirmPassword != password -> UiText.StringResource(R.string.register_confirm_password_not_match)
            else -> null
        }

        if (
            displayNameError != null || emailError != null ||
            passwordError != null || confirmPasswordError != null
        ) {
            setState {
                copy(
                    displayNameError = displayNameError,
                    emailError = emailError,
                    passwordError = passwordError,
                    confirmPasswordError = confirmPasswordError,
                    submitError = null
                )
            }
            return
        }

        if (currentState.isSubmitting) {
            return
        }

        launchOn {
            setState {
                copy(
                    isSubmitting = true,
                    displayNameError = null,
                    emailError = null,
                    passwordError = null,
                    confirmPasswordError = null,
                    submitError = null,
                    successMessage = null
                )
            }

            registerUseCase(
                RegisterParams(
                    displayName = displayName,
                    email = email,
                    password = password
                )
            ).onSuccess { session ->
                setState {
                    copy(
                        isSubmitting = false,
                        successMessage = UiText.StringResource(
                            R.string.register_success_message,
                            listOf(session.displayName)
                        )
                    )
                }
                sendEffect {
                    RegisterEffect.ShowMessage(
                        UiText.StringResource(R.string.register_success_toast)
                    )
                }
            }.onError { error ->
                setState {
                    copy(
                        isSubmitting = false,
                        submitError = error.asUiText()
                    )
                }
            }
        }
    }

    private fun clearSuccessState() {
        setState {
            copy(successMessage = null)
        }
        sendEffect { RegisterEffect.NavigateToHome }
    }

    private fun ValidationResult.toUiTextOrNull(): UiText? {
        return when (this) {
            ValidationResult.Valid -> null
            is ValidationResult.Invalid -> message
        }
    }
}

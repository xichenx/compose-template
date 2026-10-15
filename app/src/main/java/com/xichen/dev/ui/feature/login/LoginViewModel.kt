package com.xichen.dev.ui.feature.login

import com.xichen.dev.R
import com.xichen.dev.domain.model.onError
import com.xichen.dev.domain.model.onSuccess
import com.xichen.dev.domain.usecase.LoginParams
import com.xichen.dev.domain.usecase.LoginUseCase
import com.xichen.dev.domain.validation.AuthValidator
import com.xichen.dev.domain.validation.ValidationResult
import com.xichen.dev.ui.common.UiText
import com.xichen.dev.ui.common.asUiText
import com.xichen.dev.ui.mvi.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val authValidator: AuthValidator
) : BaseViewModel<LoginState, LoginEffect>(LoginState()) {

    fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.EmailChanged -> updateEmail(intent.value)
            is LoginIntent.PasswordChanged -> updatePassword(intent.value)
            LoginIntent.TogglePasswordVisibility -> togglePasswordVisibility()
            LoginIntent.Submit -> submit()
            LoginIntent.OpenRegister -> sendEffect { LoginEffect.NavigateToRegister }
            LoginIntent.DismissSuccess -> clearSuccessState()
            LoginIntent.BackClick -> sendEffect { LoginEffect.NavigateBack }
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

    private fun submit() {
        val email = currentState.email.trim()
        val password = currentState.password
        val emailError = authValidator.validateEmail(email).toUiTextOrNull()
        val passwordError = authValidator.validatePassword(password).toUiTextOrNull()
        if (emailError != null || passwordError != null) {
            setState {
                copy(
                    emailError = emailError,
                    passwordError = passwordError,
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
                    emailError = null,
                    passwordError = null,
                    submitError = null,
                    successMessage = null
                )
            }
            loginUseCase(
                LoginParams(
                    email = email,
                    password = password
                )
            ).onSuccess { session ->
                setState {
                    copy(
                        isSubmitting = false,
                        loggedInUserName = session.displayName,
                        successMessage = UiText.StringResource(
                            R.string.login_success_message,
                            listOf(session.displayName)
                        )
                    )
                }
                sendEffect {
                    LoginEffect.ShowMessage(
                        UiText.StringResource(R.string.login_success_toast)
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
        sendEffect { LoginEffect.NavigateToHome }
    }

    private fun ValidationResult.toUiTextOrNull(): UiText? {
        return when (this) {
            ValidationResult.Valid -> null
            is ValidationResult.Invalid -> message
        }
    }
}

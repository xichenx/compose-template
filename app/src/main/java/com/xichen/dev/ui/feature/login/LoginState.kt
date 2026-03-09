package com.xichen.dev.ui.feature.login

import com.xichen.dev.ui.common.UiText
import com.xichen.dev.ui.mvi.UiState

data class LoginState(
    val email: String = DEFAULT_EMAIL,
    val password: String = DEFAULT_PASSWORD,
    val isPasswordVisible: Boolean = false,
    val isSubmitting: Boolean = false,
    val emailError: UiText? = null,
    val passwordError: UiText? = null,
    val submitError: UiText? = null,
    val successMessage: UiText? = null,
    val loggedInUserName: String? = null
) : UiState {
    val canSubmit: Boolean
        get() = email.isNotBlank() && password.isNotBlank() && !isSubmitting

    companion object {
        const val DEFAULT_EMAIL = "demo@compose.dev"
        const val DEFAULT_PASSWORD = "password123"
    }
}

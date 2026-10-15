package com.xichen.dev.ui.feature.register

import com.xichen.dev.ui.common.UiText
import com.xichen.dev.ui.mvi.UiState

data class RegisterState(
    val displayName: String = DEFAULT_NAME,
    val email: String = DEFAULT_EMAIL,
    val password: String = DEFAULT_PASSWORD,
    val confirmPassword: String = DEFAULT_PASSWORD,
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isSubmitting: Boolean = false,
    val displayNameError: UiText? = null,
    val emailError: UiText? = null,
    val passwordError: UiText? = null,
    val confirmPasswordError: UiText? = null,
    val submitError: UiText? = null,
    val successMessage: UiText? = null
) : UiState {
    val canSubmit: Boolean
        get() = displayName.isNotBlank() && email.isNotBlank() && password.isNotBlank() &&
            confirmPassword.isNotBlank() && !isSubmitting

    companion object {
        const val DEFAULT_NAME = "Compose User"
        const val DEFAULT_EMAIL = "new@compose.dev"
        const val DEFAULT_PASSWORD = "password123"
    }
}

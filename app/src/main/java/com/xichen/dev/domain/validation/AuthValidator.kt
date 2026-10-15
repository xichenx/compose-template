package com.xichen.dev.domain.validation

import com.xichen.dev.R
import com.xichen.dev.ui.common.UiText
import javax.inject.Inject

class AuthValidator @Inject constructor() {

    fun validateEmail(email: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult.Invalid(UiText.StringResource(R.string.login_email_required))
            !EMAIL_REGEX.matches(email) -> ValidationResult.Invalid(UiText.StringResource(R.string.login_email_invalid))
            else -> ValidationResult.Valid
        }
    }

    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult.Invalid(UiText.StringResource(R.string.login_password_required))
            password.length < MIN_PASSWORD_LENGTH -> ValidationResult.Invalid(UiText.StringResource(R.string.login_password_invalid))
            else -> ValidationResult.Valid
        }
    }

    private companion object {
        val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        const val MIN_PASSWORD_LENGTH = 6
    }
}

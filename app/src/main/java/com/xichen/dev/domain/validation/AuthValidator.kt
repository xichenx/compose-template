package com.xichen.dev.domain.validation

import javax.inject.Inject

class AuthValidator @Inject constructor() {

    fun validateEmail(email: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult.Invalid(AuthValidationError.EmailRequired)
            !EMAIL_REGEX.matches(email) -> ValidationResult.Invalid(AuthValidationError.EmailInvalid)
            else -> ValidationResult.Valid
        }
    }

    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult.Invalid(AuthValidationError.PasswordRequired)
            password.length < MIN_PASSWORD_LENGTH -> ValidationResult.Invalid(AuthValidationError.PasswordTooShort)
            else -> ValidationResult.Valid
        }
    }

    private companion object {
        val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        const val MIN_PASSWORD_LENGTH = 6
    }
}

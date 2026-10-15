package com.xichen.dev.domain.validation

sealed interface ValidationResult {
    data object Valid : ValidationResult
    data class Invalid(val reason: AuthValidationError) : ValidationResult
}

enum class AuthValidationError {
    EmailRequired,
    EmailInvalid,
    PasswordRequired,
    PasswordTooShort
}

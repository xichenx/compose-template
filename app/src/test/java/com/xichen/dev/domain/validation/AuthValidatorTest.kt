package com.xichen.dev.domain.validation

import org.junit.Assert.assertEquals
import org.junit.Test

class AuthValidatorTest {
    private val validator = AuthValidator()

    @Test
    fun emailValidationReturnsDomainErrors() {
        assertEquals(ValidationResult.Invalid(AuthValidationError.EmailRequired), validator.validateEmail(" "))
        assertEquals(ValidationResult.Invalid(AuthValidationError.EmailInvalid), validator.validateEmail("invalid"))
        assertEquals(ValidationResult.Valid, validator.validateEmail("demo@compose.dev"))
        assertEquals(ValidationResult.Valid, validator.validateEmail("user+tag@example.com"))
    }

    @Test
    fun passwordValidationChecksMinimumBoundary() {
        assertEquals(ValidationResult.Invalid(AuthValidationError.PasswordRequired), validator.validatePassword(""))
        assertEquals(ValidationResult.Invalid(AuthValidationError.PasswordTooShort), validator.validatePassword("12345"))
        assertEquals(ValidationResult.Valid, validator.validatePassword("123456"))
    }
}

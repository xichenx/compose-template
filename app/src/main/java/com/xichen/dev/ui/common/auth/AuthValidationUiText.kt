package com.xichen.dev.ui.common.auth

import com.xichen.dev.R
import com.xichen.dev.domain.validation.AuthValidationError
import com.xichen.dev.domain.validation.ValidationResult
import com.xichen.dev.ui.common.UiText

fun ValidationResult.toUiTextOrNull(): UiText? {
    return when (this) {
        ValidationResult.Valid -> null
        is ValidationResult.Invalid -> UiText.StringResource(
            when (reason) {
                AuthValidationError.EmailRequired -> R.string.login_email_required
                AuthValidationError.EmailInvalid -> R.string.login_email_invalid
                AuthValidationError.PasswordRequired -> R.string.login_password_required
                AuthValidationError.PasswordTooShort -> R.string.login_password_invalid
            }
        )
    }
}

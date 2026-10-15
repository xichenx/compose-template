package com.xichen.dev.domain.validation

import com.xichen.dev.ui.common.UiText

sealed interface ValidationResult {
    data object Valid : ValidationResult
    data class Invalid(val message: UiText) : ValidationResult
}

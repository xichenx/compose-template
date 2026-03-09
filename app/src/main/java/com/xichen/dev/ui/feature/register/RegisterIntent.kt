package com.xichen.dev.ui.feature.register

import com.xichen.dev.ui.mvi.UiIntent

sealed interface RegisterIntent : UiIntent {
    data class DisplayNameChanged(val value: String) : RegisterIntent
    data class EmailChanged(val value: String) : RegisterIntent
    data class PasswordChanged(val value: String) : RegisterIntent
    data class ConfirmPasswordChanged(val value: String) : RegisterIntent
    data object TogglePasswordVisibility : RegisterIntent
    data object ToggleConfirmPasswordVisibility : RegisterIntent
    data object Submit : RegisterIntent
    data object DismissSuccess : RegisterIntent
    data object BackClick : RegisterIntent
}

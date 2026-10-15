package com.xichen.dev.ui.feature.login

import com.xichen.dev.ui.mvi.UiIntent

sealed interface LoginIntent : UiIntent {
    data class EmailChanged(val value: String) : LoginIntent
    data class PasswordChanged(val value: String) : LoginIntent
    data object TogglePasswordVisibility : LoginIntent
    data object Submit : LoginIntent
    data object OpenRegister : LoginIntent
    data object DismissSuccess : LoginIntent
    data object BackClick : LoginIntent
}

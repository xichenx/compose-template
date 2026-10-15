package com.xichen.dev.ui.feature.authentry

import com.xichen.dev.ui.mvi.UiEffect

sealed interface AuthEntryEffect : UiEffect {
    data object NavigateToLogin : AuthEntryEffect
    data object NavigateToRegister : AuthEntryEffect
}

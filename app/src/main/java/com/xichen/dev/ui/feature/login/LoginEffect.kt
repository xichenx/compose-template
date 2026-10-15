package com.xichen.dev.ui.feature.login

import com.xichen.dev.ui.common.UiText
import com.xichen.dev.ui.mvi.UiEffect

sealed interface LoginEffect : UiEffect {
    data object NavigateBack : LoginEffect
    data object NavigateToHome : LoginEffect
    data object NavigateToRegister : LoginEffect
    data class ShowMessage(val message: UiText) : LoginEffect
}

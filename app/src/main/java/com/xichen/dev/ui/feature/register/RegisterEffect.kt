package com.xichen.dev.ui.feature.register

import com.xichen.dev.ui.common.UiText
import com.xichen.dev.ui.mvi.UiEffect

sealed interface RegisterEffect : UiEffect {
    data object NavigateBack : RegisterEffect
    data object NavigateToHome : RegisterEffect
    data class ShowMessage(val message: UiText) : RegisterEffect
}

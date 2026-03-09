package com.xichen.dev.ui.feature.home

import com.xichen.dev.ui.common.UiText
import com.xichen.dev.ui.mvi.UiEffect

sealed interface HomeEffect : UiEffect {
    data object NavigateToLogin : HomeEffect
    data object RecreateActivity : HomeEffect
    data class ShowMessage(val message: UiText) : HomeEffect
}

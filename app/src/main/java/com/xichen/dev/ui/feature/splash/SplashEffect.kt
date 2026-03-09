package com.xichen.dev.ui.feature.splash

import com.xichen.dev.ui.mvi.UiEffect

sealed interface SplashEffect : UiEffect {
    data object NavigateToLogin : SplashEffect
    data object NavigateToHome : SplashEffect
}

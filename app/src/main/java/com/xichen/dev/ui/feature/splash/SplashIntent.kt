package com.xichen.dev.ui.feature.splash

import com.xichen.dev.ui.mvi.UiIntent

sealed interface SplashIntent : UiIntent {
    data object CheckSession : SplashIntent
}

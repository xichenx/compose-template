package com.xichen.dev.ui.feature.splash

import com.xichen.dev.ui.common.UiText
import com.xichen.dev.ui.mvi.UiState

data class SplashState(
    val title: UiText = UiText.StringResource(com.xichen.dev.R.string.splash_title),
    val subtitle: UiText = UiText.StringResource(com.xichen.dev.R.string.splash_subtitle),
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null
) : UiState

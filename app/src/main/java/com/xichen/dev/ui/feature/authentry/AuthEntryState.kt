package com.xichen.dev.ui.feature.authentry

import com.xichen.dev.ui.mvi.UiState

data class AuthEntryState(
    val isLoading: Boolean = false
) : UiState

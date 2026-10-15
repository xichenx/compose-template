package com.xichen.dev.ui.feature.authentry

import com.xichen.dev.ui.mvi.UiIntent

sealed interface AuthEntryIntent : UiIntent {
    data object OpenLogin : AuthEntryIntent
    data object OpenRegister : AuthEntryIntent
}

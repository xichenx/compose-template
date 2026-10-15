package com.xichen.dev.ui.feature.authentry

import com.xichen.dev.ui.mvi.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthEntryViewModel @Inject constructor() :
    BaseViewModel<AuthEntryState, AuthEntryEffect>(AuthEntryState()) {

    fun onIntent(intent: AuthEntryIntent) {
        when (intent) {
            AuthEntryIntent.OpenLogin -> sendEffect { AuthEntryEffect.NavigateToLogin }
            AuthEntryIntent.OpenRegister -> sendEffect { AuthEntryEffect.NavigateToRegister }
        }
    }
}

package com.xichen.dev.ui.feature.splash

import com.xichen.dev.domain.model.onError
import com.xichen.dev.domain.model.onSuccess
import com.xichen.dev.domain.usecase.GetCachedSessionUseCase
import com.xichen.dev.ui.common.asUiText
import com.xichen.dev.ui.mvi.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getCachedSessionUseCase: GetCachedSessionUseCase
) : BaseViewModel<SplashState, SplashEffect>(SplashState()) {
    private var isCheckingSession = false
    private var hasNavigated = false

    fun onIntent(intent: SplashIntent) {
        when (intent) {
            SplashIntent.CheckSession -> checkSession()
        }
    }

    private fun checkSession() {
        if (isCheckingSession || hasNavigated) return
        isCheckingSession = true
        setState { copy(isLoading = true, errorMessage = null) }
        launchOn {
            try {
                getCachedSessionUseCase()
                    .onSuccess { session ->
                        hasNavigated = true
                        setState { copy(isLoading = false) }
                        sendEffect {
                            if (session == null) SplashEffect.NavigateToLogin else SplashEffect.NavigateToHome
                        }
                    }
                    .onError { error ->
                        setState { copy(isLoading = false, errorMessage = error.asUiText()) }
                    }
            } finally {
                isCheckingSession = false
            }
        }
    }
}

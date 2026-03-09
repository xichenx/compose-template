package com.xichen.dev

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xichen.dev.domain.model.AppThemeMode
import com.xichen.dev.domain.usecase.ObserveAppThemeModeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class MainViewModel @Inject constructor(
    observeAppThemeModeUseCase: ObserveAppThemeModeUseCase
) : ViewModel() {

    val state: StateFlow<MainState> = observeAppThemeModeUseCase()
        .map { themeMode -> MainState(themeMode = themeMode) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = MainState(themeMode = AppThemeMode.default)
        )
}

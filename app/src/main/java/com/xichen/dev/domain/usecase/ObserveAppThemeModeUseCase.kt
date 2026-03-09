package com.xichen.dev.domain.usecase

import com.xichen.dev.domain.model.AppThemeMode
import com.xichen.dev.domain.repository.ScaffoldRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveAppThemeModeUseCase @Inject constructor(
    private val scaffoldRepository: ScaffoldRepository
) {
    operator fun invoke(): Flow<AppThemeMode> {
        return scaffoldRepository.observeAppThemeMode()
    }
}

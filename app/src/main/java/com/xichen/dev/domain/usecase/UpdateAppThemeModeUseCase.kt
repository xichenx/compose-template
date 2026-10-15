package com.xichen.dev.domain.usecase

import com.xichen.dev.domain.model.AppResult
import com.xichen.dev.domain.model.AppThemeMode
import com.xichen.dev.domain.repository.ScaffoldRepository
import com.xichen.dev.domain.usecase.base.BaseSuspendUseCase
import javax.inject.Inject

class UpdateAppThemeModeUseCase @Inject constructor(
    private val scaffoldRepository: ScaffoldRepository
) : BaseSuspendUseCase<AppThemeMode, Unit>() {
    override suspend fun execute(params: AppThemeMode): AppResult<Unit> {
        return scaffoldRepository.updateAppThemeMode(params)
    }
}

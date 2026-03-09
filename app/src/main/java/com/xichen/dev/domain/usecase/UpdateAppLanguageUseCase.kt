package com.xichen.dev.domain.usecase

import com.xichen.dev.domain.model.AppLanguage
import com.xichen.dev.domain.model.AppResult
import com.xichen.dev.domain.repository.LanguageRepository
import com.xichen.dev.domain.usecase.base.BaseSuspendUseCase
import javax.inject.Inject

class UpdateAppLanguageUseCase @Inject constructor(
    private val languageRepository: LanguageRepository
) : BaseSuspendUseCase<AppLanguage, Unit>() {
    override suspend fun execute(params: AppLanguage): AppResult<Unit> {
        languageRepository.updateAppLanguage(params)
        return AppResult.Success(Unit)
    }
}

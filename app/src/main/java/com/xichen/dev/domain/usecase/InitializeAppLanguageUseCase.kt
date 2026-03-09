package com.xichen.dev.domain.usecase

import com.xichen.dev.domain.model.AppResult
import com.xichen.dev.domain.repository.LanguageRepository
import com.xichen.dev.domain.usecase.base.BaseSuspendNoParamsUseCase
import javax.inject.Inject

class InitializeAppLanguageUseCase @Inject constructor(
    private val languageRepository: LanguageRepository
) : BaseSuspendNoParamsUseCase<Unit>() {
    override suspend fun execute(): AppResult<Unit> {
        languageRepository.initializeAppLanguage()
        return AppResult.Success(Unit)
    }
}

package com.xichen.dev.domain.usecase

import com.xichen.dev.domain.model.AppLanguage
import com.xichen.dev.domain.model.AppResult
import com.xichen.dev.domain.repository.LanguageRepository
import com.xichen.dev.domain.usecase.base.BaseSuspendNoParamsUseCase
import javax.inject.Inject

class GetAppLanguageUseCase @Inject constructor(
    private val languageRepository: LanguageRepository
) : BaseSuspendNoParamsUseCase<AppLanguage>() {
    override suspend fun execute(): AppResult<AppLanguage> {
        return AppResult.Success(languageRepository.getAppLanguage())
    }
}
